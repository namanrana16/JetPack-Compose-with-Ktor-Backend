package com.example.borutomid.presentation.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.borutomid.R
import com.example.borutomid.ui.theme.StarColor


@Composable
fun RatingWidget(modifier: Modifier, rating:Double,

                 scaleFactor: Float=2f, spaceBetween:Dp=6.dp)
{

    val result = calculatestars(rating = rating)
    val starPathString = stringResource(id = R.string.star_path)
    val starPath= remember{
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(starPathString).toPath()

    }
    val starPathBounds= remember{
        starPath.getBounds()
    }
    
 Row(modifier = modifier,
     horizontalArrangement = Arrangement.spacedBy(spaceBetween))
 {
     result["filledStars"]?.let { repeat(it){ FilledStar(
         starPath = starPath,
         starPathBounds = starPathBounds,
         scaleFactor = 3f
     )} }

     result["halfFilledStars"]?.let { repeat(it){ HalfFilledStar(
         starPath = starPath,
         starPathBounds = starPathBounds,
         scaleFactor = 3f
     )
     } }

     result["emptyStars"]?.let { repeat(it){ EmptyStar(
         starPath = starPath,
         starPathBounds = starPathBounds,
         scaleFactor = 3f
     )} }

 }

}



@Composable
fun EmptyStar(starPath: androidx.compose.ui.graphics.Path, starPathBounds: androidx.compose.ui.geometry.Rect,
              scaleFactor:Float)
{
    Canvas(modifier = androidx.compose.ui.Modifier.size(24.dp)){


        scale(scale = scaleFactor){
            val canvasSize=this.size
            val pathWidth=starPathBounds.width
            val pathHeight=starPathBounds.height
            val left = (canvasSize.width/2)-(pathWidth/1.7f)
            val top = (canvasSize.height/2)-(pathHeight/1.7f)
            translate(left = left,top=top) {
                drawPath(path=starPath, color = Color.LightGray.copy(alpha = 0.5f))
            }
        }
    }
}



@Composable
fun FilledStar(
    starPath: androidx.compose.ui.graphics.Path, starPathBounds: androidx.compose.ui.geometry.Rect,
    scaleFactor:Float)
{
    Canvas(modifier = androidx.compose.ui.Modifier.size(24.dp)){


       scale(scale = scaleFactor){
           val canvasSize=this.size
           val pathWidth=starPathBounds.width
           val pathHeight=starPathBounds.height
           val left = (canvasSize.width/2)-(pathWidth/1.7f)
           val top = (canvasSize.height/2)-(pathHeight/1.7f)
           translate(left = left,top=top) {
               drawPath(path=starPath, color = StarColor)
           }
       }
    }
}

@Composable
fun HalfFilledStar(starPath: androidx.compose.ui.graphics.Path, starPathBounds: androidx.compose.ui.geometry.Rect,
                   scaleFactor:Float)
{


    Canvas(modifier = androidx.compose.ui.Modifier.size(24.dp)){


        scale(scale = scaleFactor){
            val canvasSize=this.size
            val pathWidth=starPathBounds.width
            val pathHeight=starPathBounds.height
            val left = (canvasSize.width/2)-(pathWidth/1.7f)
            val top = (canvasSize.height/2)-(pathHeight/1.7f)
            translate(left = left,top=top) {
                drawPath(path=starPath, color = Color.LightGray.copy(alpha = 0.5f))
                clipPath(path=starPath){
                    drawRect(color= StarColor,
                        size = Size(width = starPathBounds.maxDimension/1.7f,
                        height = starPathBounds.maxDimension*scaleFactor))
                }
            }
        }
    }
}

@Composable
fun calculatestars(rating:Double):Map<String,Int> {
    val maxStars by remember { mutableStateOf(5) }
    var filledStars by remember { mutableStateOf(0) }
    var halfFilledStars by remember { mutableStateOf(0) }
    var emptyStars by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = rating) {

        val (firstNumber, secondNumber) = rating.toString().split(".")
            .map { it.toInt() }

        if (firstNumber in 0..5 && secondNumber in 0..9) {
            filledStars = firstNumber
            if (secondNumber in 1..5) {
                halfFilledStars++
            }
            if (secondNumber in 6..9)
                filledStars++

            if (firstNumber == 5 && secondNumber > 0) {
                emptyStars = 5
                filledStars = 0
                halfFilledStars = 0
            } else {
                Log.d("RatingWidget", "Invalid Rating")

            }


        }
    }
    emptyStars = maxStars - (filledStars + halfFilledStars)
    return mapOf(
        "filledStars" to filledStars,
        "halfFilledStars" to halfFilledStars,
        "emptyStars" to emptyStars
    )
}



@Composable
@Preview
    (showBackground = true)
fun FilledStarPreview()
{
    RatingWidget(
        modifier = androidx.compose.ui.Modifier,
        rating = 1.0
    )
}

@Composable
@Preview
    (showBackground = true)
fun HalfFilledStarPreview()
{
    val starPathString = stringResource(id = R.string.star_path)
    val starPath= remember{
        androidx.compose.ui.graphics.vector.PathParser().parsePathString(starPathString).toPath()

    }
    val starPathBounds= remember{
        starPath.getBounds()
    }


    HalfFilledStar(starPath =starPath , starPathBounds =starPathBounds, scaleFactor = 2f )
}