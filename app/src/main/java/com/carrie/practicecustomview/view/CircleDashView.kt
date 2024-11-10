import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.carrie.practicecustomview.view.RADIUS


class CircleDashView (
    context: Context
) : View(context) {

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val circlePath: Path = Path()
    private val dashPathEffect: DashPathEffect
    private  val OPEN_ANGLE = 120f
    private  val START_ANGLE = -180 - (180 - OPEN_ANGLE) / 2

    init {
        paint.color = Color.BLACK
        paint.strokeWidth = 5f
        paint.style = Paint.Style.STROKE

        // Initialize the circular path (circle)
//        circlePath.addCircle(300f, 300f, 150f, Path.Direction.CW)
        circlePath.addArc(width / 2f - RADIUS, height / 2f - RADIUS, width / 2f + RADIUS, height / 2f + RADIUS, START_ANGLE, 360 - OPEN_ANGLE)

        // Set up the DashPathEffect
        val dashLength = 20f
        val gapLength = 10f
        dashPathEffect = DashPathEffect(floatArrayOf(dashLength, gapLength), 0f)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Apply the DashPathEffect to the paint
        paint.pathEffect = dashPathEffect

        // Draw the circle path with the applied DashPathEffect
        canvas.drawPath(circlePath, paint)
    }




}
