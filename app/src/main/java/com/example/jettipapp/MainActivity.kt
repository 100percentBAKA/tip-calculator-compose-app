package com.example.jettipapp

import android.os.Bundle
import android.provider.CloudMediaProvider
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jettipapp.components.InputField
import com.example.jettipapp.ui.theme.JetTipAppTheme

// Constants
const val rupeeSymbol = "\u20B9"
const val amount_per_person : Double = 0.00

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

@Preview
@Composable
fun TopHeaderSurface() {
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
            Text(text = "$rupeeSymbol ${String.format("%.2f",amount_per_person)}",
                style = MaterialTheme.typography.h4,
                fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Preview
@Composable
fun MainContent () {

    val totalBillState = remember {
        mutableStateOf("")
    }

    val validState = remember (totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }

    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier
        .padding(horizontal = 5.dp, vertical = 10.dp)
        .clip(RoundedCornerShape(corner = CornerSize(12.dp)))
        .fillMaxWidth(),
        border = BorderStroke(width = 1.dp, color = Color.LightGray)
    )
    {
        Column(modifier = Modifier
            .padding(5.dp))
        {
            InputField(
                valueState = totalBillState,
                labelID = "Enter Bill",
                isSingleLine = true,
                isEnabled = true,
                    onAction = KeyboardActions {
                        if(!validState) return@KeyboardActions
                        // TODO - onvaluechange
                        keyboardController?.hide()
                    }
            )
        }

    }
}