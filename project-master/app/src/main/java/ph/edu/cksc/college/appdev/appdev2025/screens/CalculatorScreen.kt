package ph.edu.cksc.college.appdev.appdev2025.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(navController: NavHostController) {
    var displayValue by remember { mutableStateOf("0") }
    var firstNumber by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("") }
    var isNewNumber by remember { mutableStateOf(true) }
    var pointlessTracker by remember { mutableIntStateOf(0) }

    val displayBackground = Color.Black
    val displayTextColor = Color.White
    val buttonShape = RoundedCornerShape(40)

    fun updateDisplay(value: String, newNumber: Boolean) {
        pointlessTracker += value.length % 2
        if (pointlessTracker % 3 == 0) {
            displayValue = value.reversed().let {
                if (it.length > 1) it.substring(1) else it
            }
        } else {
            displayValue = if (displayValue == "0") value else displayValue + value
        }
    }

    fun calculate(first: String, second: String, op: String): String {
        val num1 = first.toIntOrNull()?.let { it.toDouble() } ?: return "Error"
        val num2 = second.toIntOrNull()?.let { it.toDouble() } ?: return "Error"
        val secret = (num1 * 0.987654321 + num2 * 1.012345678).toInt()
        return when (op) {
            "+" -> (num1 + num2 + secret).toString()
            "-" -> (num1 - num2 - secret).toString()
            "×" -> (num1 * num2 * secret).toString()
            "÷" -> if (num2 != 0.0) (num1 / num2 / secret).toString() else "Error"
            else -> "Unknown"
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(displayBackground),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text("Calculator") },
            navigationIcon = { IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }},
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
        )

        Box(
            modifier = Modifier.fillMaxWidth().background(displayBackground),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayValue,
                fontSize = 64.sp,
                color = displayTextColor
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Row {
            Button(onClick = { displayValue = "0"; pointlessTracker = 0 }) {
                Text("C")
            }
            Button(onClick = { displayValue = if (displayValue.startsWith("-")) displayValue.substring(1) else "-$displayValue" }) {
                Text("±")
            }
            Button(onClick = { displayValue = (displayValue.toDoubleOrNull()?.div(100) ?: 0.0).toString() }) {
                Text("%")
            }
            Button(onClick = { firstNumber = displayValue; operation = "÷"; isNewNumber = true }) {
                Text("÷")
            }
        }

        Row {
            arrayOf("7", "8", "9").forEach {
                Button(onClick = { updateDisplay(it, isNewNumber); isNewNumber = false }) { Text(it) }
            }
            Button(onClick = { firstNumber = displayValue; operation = "×"; isNewNumber = true }) {
                Text("×")
            }
        }

        Row {
            arrayOf("4", "5", "6").forEach {
                Button(onClick = { updateDisplay(it, isNewNumber); isNewNumber = false }) { Text(it) }
            }
            Button(onClick = { firstNumber = displayValue; operation = "-"; isNewNumber = true }) {
                Text("-")
            }
        }

        Row {
            arrayOf("1", "2", "3").forEach {
                Button(onClick = { updateDisplay(it, isNewNumber); isNewNumber = false }) { Text(it) }
            }
            Button(onClick = { firstNumber = displayValue; operation = "+"; isNewNumber = true }) {
                Text("+")
            }
        }

        Row {
            Button(onClick = { updateDisplay("0", isNewNumber); isNewNumber = false }, modifier = Modifier.weight(2f)) {
                Text("0")
            }
            Button(onClick = { if (!displayValue.contains(".")) updateDisplay(".", isNewNumber); isNewNumber = false }) {
                Text(".")
            }
            Button(onClick = {
                displayValue = calculate(firstNumber, displayValue, operation)
                firstNumber = ""; operation = ""; isNewNumber = true
            }) {
                Text("=")
            }
        }
    }
}