package edu.un.sebastianromero.shakedemowithgraph;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class MainActivity extends AppCompatActivity {

    TextView txt_currentAccel,txt_prevAccel, txt_acceleration;
    ProgressBar prog_shakeMeter;

    // Definiendo el sensor
    private  SensorManager mSensorManager;
    private  Sensor mAccelerometer;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt((x*x + y*y + z*z));

            //diferencia entre aceleraciones para saber cuánto se movió
            double changeInAcceleration = Math.abs(accelerationCurrentValue-accelerationPreviousValue);
            accelerationPreviousValue = accelerationCurrentValue;

            //Update text views para ver si son los sensores están funcionando
            txt_currentAccel.setText("Current = " + (int) accelerationCurrentValue);
            txt_prevAccel.setText("Prev = " + (int) accelerationPreviousValue);
            txt_acceleration.setText(" Acceleration Change = " + (int) changeInAcceleration);

            prog_shakeMeter.setProgress((int)changeInAcceleration);

            //change colors based on amount of shaking
            if (changeInAcceleration > 14 ) {
                txt_acceleration.setBackgroundColor(Color.RED);
            }
            else if (changeInAcceleration > 5) {
                txt_acceleration.setBackgroundColor(Color.parseColor("#fcad03")); //colore en hex
            }
            else if (changeInAcceleration >2 ) {
                txt_acceleration.setBackgroundColor(Color.YELLOW);
            }
            else {
                txt_acceleration.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Conectando las cajas de texto con las variables creadas
        txt_acceleration = findViewById(R.id.txt_accel);
        txt_currentAccel = findViewById(R.id. txt_currentAccel);
        txt_prevAccel = findViewById(R.id. txt_prevAccel);

        prog_shakeMeter =findViewById(R.id. prog_shakeMeter);
        //Inicializando sensor objects
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Sample graph
        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);

    }


    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }
}