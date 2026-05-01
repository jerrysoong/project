package ph.edu.cksc.college.appdev.appdev2025.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import ph.edu.cksc.college.appdev.appdev2025.data.TaskModel

class TaskStorageService(
    val auth: FirebaseAuth,
    val firestore: FirebaseFirestore
) {
    fun getTasks(): Flow<List<TaskModel>> = flow {
        val snapshot = firestore
            .collection(TASKENTRY_COLLECTION)
            .orderBy(TIMESTAMP_FIELD, Query.Direction.DESCENDING)
            .get().await()
        val list: MutableList<TaskModel> = ArrayList()
        for (document in snapshot) {
            val data = document.data
            val entry = TaskModel(
                id = document.id,
                title = data["title"] as? String ?: "",
                description = data["description"] as? String ?: "",
                isDone = data["isDone"] as? Boolean ?: false,
                timestamp = data["timestamp"] as? String ?: ""
            )
            list.add(entry)
        }
        emit(list)
    }

    suspend fun getTaskEntry(taskEntryId: String): TaskModel? =
        toObject(firestore.collection(TASKENTRY_COLLECTION).document(taskEntryId).get().await())

    private fun toObject(document: DocumentSnapshot?): TaskModel? {
        if (document == null) return null
        val data = document.data
        return TaskModel(
            id = document.id,
            title = data?.get("title") as? String ?: "",
            description = data?.get("description") as? String ?: "",
            isDone = data?.get("isDone") as? Boolean ?: false,
            timestamp = data?.get("timestamp") as? String ?: ""
        )
    }

    suspend fun save(taskEntry: TaskModel): String {
        val data = hashMapOf(
            "title" to taskEntry.title,
            "description" to taskEntry.description,
            "isDone" to taskEntry.isDone,
            "timestamp" to taskEntry.timestamp
        )
        firestore.collection(TASKENTRY_COLLECTION).add(data).await()
        return ""
    }

    suspend fun update(taskEntry: TaskModel) {
        val data = hashMapOf(
            "title" to taskEntry.title,
            "description" to taskEntry.description,
            "isDone" to taskEntry.isDone,
            "timestamp" to taskEntry.timestamp
        )
        firestore.collection(TASKENTRY_COLLECTION).document(taskEntry.id).set(data).await()
    }

    suspend fun delete(taskEntryId: String) {
        firestore.collection(TASKENTRY_COLLECTION).document(taskEntryId).delete().await()
    }

    suspend fun clearAll() {
        val snapshot = firestore.collection(TASKENTRY_COLLECTION).get().await()
        for (document in snapshot.documents) {
            firestore.collection(TASKENTRY_COLLECTION).document(document.id).delete().await()
        }
    }

    companion object {
        private const val TIMESTAMP_FIELD = "timestamp"
        private const val TASKENTRY_COLLECTION = "taskentries"
    }
}