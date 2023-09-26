import android.app.Service
import android.content.Context
import android.content.Intent
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.dmiranda.dg_foregroundservice.R
import com.dmiranda.dg_foregroundservice.MainActivity
import timber.log.Timber

private const val CHANNEL_ID = "ForegroundService"
class ForegroundService : Service() {

    private val mHandler = Handler()
    private lateinit var mRunnable: Runnable
    private var count = 0

    companion object {
        var running = false
        private lateinit var handlerCallbacks: Handler

        fun starService(context: Context, message: String, handler: Handler)  {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context,startIntent)
            running = true
            handlerCallbacks = handler
        }

        private fun Intent(context: String, java: Class<ForegroundService>): Intent {
            TODO("Not yet implemented")
        }


        fun stopService(context: Context)  {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)
            running = false
        }

        fun starService(context: String, message: Handler) {

        }


    }
    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("ForegroundService", "onStartCommand")
        val input = intent?.getStringExtra("inputExtra") ?: ""
        createNotificationChannel(input)
        runTask()
        return Service.START_NOT_STICKY
    }

    private fun createNotificationChannel(input: String) {
        Log.d("ForegroundService", "createNotifivationChannel")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)  {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(getString(R.string.title))
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    private fun runTask() {
        Log.d("ForegroundService", "runtask")
        val delayTime = 1000 * 7L
        mRunnable = Runnable {
            count += 1
            notifyNextEvent()
            mHandler.postDelayed(mRunnable, delayTime)
        }
        mHandler.postDelayed(mRunnable, delayTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacks(mRunnable)
        Timber.tag("ForegroundService")
            .d("onDestroy:Ejecutando OnDestroy del proceso en primer plano")
    }

    private fun notifyNextEvent() {
        TODO("Not yet implemented")
    }
}