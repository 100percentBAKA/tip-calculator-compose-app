package com.example.jettipapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jettipapp.components.IconButtons
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme
import com.example.jettipapp.util.calculatePerPerson
import com.example.jettipapp.util.calculateTipTotal

// Constants
// const val rupeeSymbol = "\u20B9"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                // TopHeaderSurface()
                MainContent()
            }
        }
    }
}

@Composable
fun MyApp(content : @Composable () -> Unit) {
    JetTipAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            color = MaterialTheme.colors.background
        )
        {
            content()
        }
    }
}

@Composable
fun TopHeaderSurface(totalPerPerson : Double) {
    Surface(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .clip(RoundedCornerShape(corner = CornerSize(12.dp))),
    color = Color(0xFFE9D7F7))
    {
        Column(modifier = Modifier
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
        {
            Text(text = "Total Per Person",
                style = MaterialTheme.typography.h5)

            // val amount_per_person = "%.2f.format(amount_per_person)
            Text(text = "$ ${String.format("%.2f",totalPerPerson)}",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent () {
    val splitByState = remember {
        mutableStateOf(1) // split value cannot be 0
    }

    val range = IntRange(start = 1, endInclusive = 100)

    val tipAmountState = remember {
        mutableStateOf(0.0) // tip amount can be 0
    }

    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }

    BillForm(
        range = range,
        splitByState = splitByState,
        tipAmountState = tipAmountState,
        totalPerPersonState = totalPerPersonState
    ) {
        billAmount ->
        Log.d("AMT", "MainContent: $billAmount")
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier : Modifier = Modifier,
    range : IntRange = 1..100, // 100 inclusive
    splitByState: MutableState <Int>,
    tipAmountState: MutableState <Double>,
    totalPerPersonState: MutableState <Double>,
    onValueChange : (String) -> Unit = {})
{
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = ((sliderPositionState.value) * 100).toInt()

    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember (totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier
        .padding(horizontal = 8.dp, vertical = 10.dp)
        .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
        .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = Color.LightGray),
        elevation = 10.dp
    )
    {
        Column(modifier = Modifier
            .padding(6.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start)
        {

            TopHeaderSurface(totalPerPerson = totalPerPersonState.value)

            InputField(
                valueState = totalBillState,
                labelID = "Enter Bill",
                isSingleLine = true,
                isEnabled = true,
                onAction = KeyboardActions {
                    if(!validState) return@KeyboardActions
                    onValueChange (totalBillState.value.trim())

                    keyboardController?.hide()
                }
            )

//            if(validState) {
                Row(modifier = Modifier
                    .padding(10.dp))
                {
                    Text(text = "Split", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 6.dp))
                    Spacer(modifier = Modifier.width(130.dp))

                    Row(horizontalArrangement = Arrangement.End)
                    {
                        IconButtons(
                            imageVector = Icons.Default.Remove,
                            onClick = {
                                Log.d("ICON-MINUS", "BillForm: Minus")
                                splitByState.value =
                                    if(splitByState.value > 1) splitByState.value -1
                                else 1
                            }
                        )
                        Text(text = "${splitByState.value}", style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .align(Alignment.CenterVertically))
                        IconButtons(
                            imageVector = Icons.Default.Add,
                            onClick = {
                                Log.d("ICON-PLUS", "BillForm: Add")
                                if (splitByState.value < range.last) {
                                    splitByState.value += 1
                                }
                            }
                        )
                    }

                }

                // Tip Row
                Row (modifier = Modifier.padding(10.dp))
                {
                    Text(text = "Tip", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(183.dp))
                    Text(text = "$ ${tipAmountState.value}", modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.h6)
                }
            
                Spacer(modifier = Modifier.height(10.dp))

                // Tip Slider Column
                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth())
                {
                    Text(text = "$tipPercentage %", style = MaterialTheme.typography.h6)
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Slider(
                        value = sliderPositionState.value,
                        onValueChange = {
                            newValue ->
                            sliderPositionState.value = newValue
                            Log.d("SLIDER", "BillForm: $newValue")

                            tipAmountState.value =
                                calculateTipTotal(totalBill = totalBillState.value.toDouble(), tipPercentage)

                            totalPerPersonState.value =
                                calculatePerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage)
                        },
                        modifier = Modifier.padding(horizontal = 16.dp),
                        steps = 5)
                }

//            }

//            else {
//                Box() { }
//            }
        }

    }
}


