import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.kotlinx.dl.onnx.inference.ONNXModelHub
import org.jetbrains.kotlinx.dl.onnx.inference.ONNXModels
import java.io.File

/**
 * Adapted for Kotlin Desktop from resources below - full rights to respective authors
 *
 * https://blog.jetbrains.com/kotlin/2021/09/kotlindl-0-3-is-out-with-onnx-integration-object-detection-api-20-new-models-in-modelhub-and-many-new-layers/#objectdetection
 *
 * https://github.com/zaleslaw/Ktor-KotlinDL-Object-Detection-examples/blob/master/src/main/kotlin/demo/objectdetection/objectDetectionSSDWithVisualisation3.kt
 */

// Test Image from resources/detection folder
const val TestFile = "image9.jpg"

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {

        // Load an image bitmap from the specified resource path
        val bitmap = useResource("/detection/$TestFile") { loadImageBitmap(it) }
        // Display the loaded image in the application window
        Image(painter = BitmapPainter(bitmap), contentDescription = "")
        val imageHeight = bitmap.height
        val imageWidth = bitmap.width

        // Initialize the ONNXModelHub with a specified cache directory
        val modelHub = ONNXModelHub(cacheDirectory = File("cache/pretrainedModels"))
        // Load the pre-trained SSD object detection model
        val model = modelHub.loadPretrainedModel(ONNXModels.ObjectDetection.SSD)
        // Alternatively, load the pre-trained model directly using the model's method
        val model2 = ONNXModels.ObjectDetection.SSD.pretrainedModel(modelHub)

        // Use the loaded model to detect objects in the specified image file
        model.use { detectionModel ->
            val imageFile = File("src/main/resources/detection/$TestFile")
            val detectedObjects = detectionModel.detectObjects(imageFile = imageFile, topK = 20)

            // Iterate through detected objects and display bounding boxes and labels for objects with a probability higher than  0.5
            detectedObjects.forEach {
                if (it.probability > 0.5){
                    val yMin = it.yMin * imageHeight
                    val xMin = it.xMin * imageWidth
                    val yMax = it.yMax * imageHeight
                    val xMax = it.xMax * imageWidth

                    // Display a bounding box around the detected object with a label showing the object's class and probability
                    Box(
                        modifier = Modifier
                            .offset(xMin.dp,yMin.dp)
                            .size((xMax-xMin).dp,(yMax-yMin).dp)
                            .background(Color.Transparent)
                            .border(width = 2.dp, Color.Green)
                    ){
                        Text("${it.label} = ${it.probability}",
                            color = Color.Green)
                    }
                    }

            }
        }
    }
}