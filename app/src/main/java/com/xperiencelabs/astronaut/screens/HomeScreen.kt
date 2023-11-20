package com.xperiencelabs.astronaut.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xperiencelabs.astronaut.R
import com.xperiencelabs.astronaut.SpeechToTextWrapper
import kotlinx.coroutines.delay

@Composable
fun HomeScreen() {
    var animation by remember {
        mutableStateOf("idle")
    }
    var listenEnable by remember {
        mutableStateOf(false)
    }

    val listeningComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.listening))
    val progress by animateLottieCompositionAsState(composition = listeningComposition, iterations = LottieConstants.IterateForever)
    ARView(animation)
     
    Box(modifier = Modifier.fillMaxSize()){
        Text(text = animation,modifier = Modifier.align(Alignment.Center))

        //Mic button
        Box(modifier = Modifier
            .size(180.dp)
            .clickable {
                listenEnable = !listenEnable
            }
            .align(Alignment.BottomCenter)
            .padding(bottom = 20.dp),
            contentAlignment = Alignment.Center
        ){
            if(listenEnable){
            LottieAnimation(composition = listeningComposition, progress = progress, modifier = Modifier.size(180.dp))
            }else {
                Box(modifier = Modifier.size(60.dp).clip(CircleShape).background(Color.Black)){
                    Icon(painter = painterResource(id = R.drawable.baseline_mic_24), contentDescription = "microphone",modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp))
                }


            }
        }
    }
    
    SpeechToTextWrapper(
        listenEnable = listenEnable,
        onSpeechStarted = { /*TODO*/ },
        onSpeechStopped = { listenEnable = false},
        onSpeechError = {
                        Log.e("SPEECH ERROR",it)
        },
        onSpeechResult = {
            animation = it.joinToString ("\n")
        }
    )

}