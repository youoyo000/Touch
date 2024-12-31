package com.example.touch

import android.os.Bundle
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.touch.ui.theme.TouchTheme
import androidx.compose.runtime.remember as remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            DrawCircle()
                DrawPath()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

    Row{
    Text(text = "多指觸控Compose實例",
        fontFamily = FontFamily(Font(R.font.finger)),
        fontSize = 25.sp,
        color = Color.Blue)
    Image(
        painter = painterResource(id = R.drawable.hand),
        contentDescription = "手掌圖片",
        alpha = 0.7f,
        modifier = Modifier
            .clip(CircleShape)
            .background(Color.Blue)
    )
}
        Text(text = "作者：李宥萱",
            fontFamily = FontFamily(Font(R.font.finger)),
            fontSize = 25.sp,
            color = Color.Black)
    }

}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawCircle() {
    var X = remember { mutableStateListOf(0f) }
    var Y = remember { mutableStateListOf(0f) }
    var Fingers by remember {  mutableStateOf (0)  }
    val handImage = ImageBitmap.imageResource(R.drawable.hand)
    val ColorRed = Color(0xFFFF0000)
    val ColorOrange = Color(0xFFFFA500)
    val ColorYellow = Color(0xFFFFFF00)
    val ColorGreen = Color(0xFF008000)
    val ColorBlue = Color(0xFF0000FF)
    val ColorIndigo = Color(0xFF4B0082)
    val ColorPurple = Color(0xFF800080)


    var PaintColor:Color
    var colors = arrayListOf(
        ColorRed, ColorOrange, ColorYellow, ColorGreen,
        ColorBlue, ColorIndigo, ColorPurple
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                Fingers = event.getPointerCount()
                X.clear()
                Y.clear()
                for (i in 0..Fingers - 1) {
                   // PaintColor = colors[i % 7]
                    X.add( event.getX(i))
                    Y.add (event.getY(i))
                }
                true
            }

    ){
        Canvas(modifier = Modifier){
           // drawCircle(Color.Yellow, 100f, Offset(X, Y))
            //drawImage(handImage, Offset(X-handImage.width/2,Y-handImage.height/2))
            for (i in 0..Fingers - 1) {
                PaintColor = colors[i % 7]
                drawCircle(PaintColor, 100f, Offset(X[i], Y[i]))
            }

        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DrawPath() {
    val paths = remember { mutableStateListOf<Points>() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        paths.clear()
                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        paths += Points(event.x, event.y)
                        true
                    }
                    else -> false
                }
            }

    ){
        Canvas(modifier = Modifier){
            val p = Path()
            //p.moveTo(500f, 300f)
            //p.lineTo(300f,600f)
            var j = 0
            for (path in paths) {
                if (j==0){  //第一筆不畫
                    p.moveTo(path.x, path.y)
                }
                else{
                    p.lineTo(path.x, path.y)
                }
                j++
            }

            drawPath(p, color = Color.Black,
                style = Stroke(width = 30f, join = StrokeJoin.Round)
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TouchTheme {
        Greeting("Android")
    }
}