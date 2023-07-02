package com.camilo

import nu.pattern.OpenCV
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.core.Scalar
import org.opencv.highgui.HighGui.imshow
import org.opencv.highgui.HighGui.waitKey
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import org.opencv.videoio.VideoCapture

const val ESC_KEY = 27
fun main() {
    OpenCV.loadShared()
    val camera = VideoCapture(0).takeIf { it.isOpened }

    if (camera == null) {
        println("Error while try to open camera")
        return
    }
    val frame = Mat()
    val windowName = "Live Video"
    val classifier = CascadeClassifier("haarcascade_frontalface_default.xml")

    while (true) {
        camera.read(frame)
        if (frame.empty()) {
            println("Unable to read frame")
            break
        }

        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2GRAY)
        Core.flip(frame, frame, 1)

        val faces = MatOfRect()
        classifier.detectMultiScale(frame, faces)
        for (rect in faces.toArray()) {
            Imgproc.rectangle(frame, rect.tl(), rect.br(), Scalar(0.0, 255.0, 0.0), 3)
        }

        imshow(windowName, frame)

        if (waitKey(1) == ESC_KEY) {
            break
        }
    }

    camera.release()
}
