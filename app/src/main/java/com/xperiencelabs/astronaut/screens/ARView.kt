package com.xperiencelabs.astronaut.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.LightEstimationMode
import io.github.sceneview.ar.node.ArModelNode
import io.github.sceneview.ar.node.ArNode
import io.github.sceneview.ar.node.PlacementMode


@Composable
fun ARView(animation:String) {

    val nodes = remember(animation) {
        mutableStateListOf<ArNode>()
    }
    val modelNode = remember {
        mutableStateOf<ArModelNode?>(null)
    }
    val placeModel = remember {
        mutableStateOf(true)
    }
    var previousAnimation by remember {
        mutableStateOf("idle")
    }

    Box(modifier = Modifier.fillMaxSize()){
        ARScene(
            modifier = Modifier.fillMaxSize(),
            nodes = nodes,
            planeRenderer = true,
            onCreate = {arSceneView ->
                arSceneView.lightEstimationMode = LightEstimationMode.DISABLED
                modelNode.value = ArModelNode(PlacementMode.INSTANT).apply {
                    loadModelGlbAsync("models/astronaut.glb", scaleToUnits = 1f) {
                        playAnimation(animation)
                    }

                    onAnchorChanged = {
                        placeModel.value = !isAnchored
                    }

                }

                nodes.add(modelNode.value!!)
            }
        )

        if(placeModel.value){
            Button(onClick = {
                modelNode.value?.anchor()
            },modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 150.dp)) {
                Text(text = "Place it")
            }
        }

    }

    LaunchedEffect(key1 = animation){
        modelNode.value?.stopAnimation(previousAnimation)
        modelNode.value?.playAnimation(animation)
        previousAnimation = animation
    }
}