package com.example.yawndetection.detector.face
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.example.yawndetection.detector.GraphicOverlay
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceLandmark

class FaceGraphic(overlay: GraphicOverlay?, private val face: Face)
  : GraphicOverlay.Graphic(overlay) {
  private val facePositionPaint: Paint
  private val numColors = COLORS.size
  private val idPaints = Array(numColors) { Paint() }
  private val boxPaints = Array(numColors) { Paint() }
  private val labelPaints = Array(numColors) { Paint() }

  init {
    val selectedColor = Color.RED
    facePositionPaint = Paint()
    facePositionPaint.color = selectedColor
    for (i in 0 until numColors) {
      idPaints[i] = Paint()
      idPaints[i].color = COLORS[i][0]
      idPaints[i].textSize = ID_TEXT_SIZE
      boxPaints[i] = Paint()
      boxPaints[i].color = COLORS[i][1]
      boxPaints[i].style = Paint.Style.STROKE
      boxPaints[i].strokeWidth = BOX_STROKE_WIDTH
      labelPaints[i] = Paint()
      labelPaints[i].color = COLORS[i][1]
      labelPaints[i].style = Paint.Style.FILL
    }
  }

  /** Draws the face annotations for position on the supplied canvas. */
  override fun draw(canvas: Canvas?) {
    val x = translateX(face.boundingBox.centerX().toFloat())
    val y = translateY(face.boundingBox.centerY().toFloat())

    val left = x - scale(face.boundingBox.width() / 2.0f)
    val top = y - scale(face.boundingBox.height() / 2.0f)
    val right = x + scale(face.boundingBox.width() / 2.0f)
    val bottom = y + scale(face.boundingBox.height() / 2.0f)
    val lineHeight = ID_TEXT_SIZE + BOX_STROKE_WIDTH

    canvas?.drawRect(left, top, right, bottom, boxPaints[0])

    if (canvas != null) {
      val mouthBottom = face.getLandmark(FaceLandmark.MOUTH_BOTTOM)
      val mouthLeft = face.getLandmark(FaceLandmark.MOUTH_LEFT)
      val mouthRight = face.getLandmark(FaceLandmark.MOUTH_RIGHT)
      val nose = face.getLandmark(FaceLandmark.NOSE_BASE)

      if (mouthRight != null && mouthBottom != null && mouthLeft != null) {
        val proportion = nose?.let {
          ((nose.position.y - mouthBottom.position.y) / (mouthLeft.position.x - mouthRight.position.x))
        }

        if (proportion != null && proportion > 0.9) {
          canvas.drawText("($proportion) ЗЕВАЕТ", left, bottom + 2 * lineHeight, idPaints[1])
        } else {
          canvas.drawText("($proportion) НЕ ЗЕВАЕТ", left, bottom + 2 * lineHeight, idPaints[0])
        }
      }
    }
  }

  companion object {
    private const val ID_TEXT_SIZE = 60.0f
    private const val BOX_STROKE_WIDTH = 5.0f
    private val COLORS =
      arrayOf(
        intArrayOf(Color.RED, Color.WHITE),
        intArrayOf(Color.GREEN, Color.WHITE)
      )
  }
}
