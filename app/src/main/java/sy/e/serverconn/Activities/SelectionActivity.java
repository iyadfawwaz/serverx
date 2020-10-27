package sy.e.serverconn.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import sy.e.serverconn.R;
import sy.iyad.mikrotik.MikrotikServer;
import sy.iyad.mikrotik.Models.ExecutionEventListener;
import static sy.e.serverconn.ServerInformations.CPU_COMMAND;
import static sy.e.serverconn.ServerInformations.RUNNING_TRUE;
import static sy.e.serverconn.ServerInformations.UPTIME_COMMAND;
import static sy.e.serverconn.ServerInformations.VOLTAGE_COMMAND;


public class SelectionActivity extends AppCompatActivity {

    Button button1;
    Button button2;
    TextView cput;
    RadioButton ether1;
    RadioButton ether10;
    RadioButton ether11;
    RadioButton ether12;
    RadioButton ether13;
    RadioButton ether2;
    RadioButton ether3;
    RadioButton ether4;
    RadioButton ether5;
    RadioButton ether6;
    RadioButton ether7;
    RadioButton ether8;
    RadioButton ether9;
    Thread ledsThread;
    Button logger;
    ReUptade reUptadeCpu;
    ReUptade reUptadeUptime;
    TaskLeds taskLeds;
    TextView uptimet;
    TextView voltaget;


    private class ReUptade extends Thread {

        public String cmdx;
        public String keys;
        public TextView textViewx;

        public ReUptade(TextView textViewx, String cmdx, String keys) {

            this.textViewx = textViewx;
            this.cmdx = cmdx;
            this.keys = keys;

        }

        public void run() {
            try {
                Thread.sleep(1000);
                SelectionActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MikrotikServer.execute(cmdx).addExecutionEventListener(new ExecutionEventListener() {
                            public void onExecutionSuccess(List<Map<String, String>> mapList) {

                                textViewx.setText( mapList.get(0).get(keys));

                            }

                            public void onExecutionFailed(Exception exp) {
                                textViewx.setText(exp.getMessage());
                            }
                        });
                    }
                });
            } catch (InterruptedException e) {
                textViewx.setText(e.getMessage());
            }
        }

        public void stopthis() {
            Thread.currentThread().interrupt();
        }
    }

    class TaskLeds implements Runnable {
        TaskLeds() {
        }

        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(1000);
                    SelectionActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            turnOnOffLeds();
                        }
                    });
                } catch (InterruptedException e) {
                    cput.setText(e.getMessage());
                }
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activit_selection);

        button1 =  findViewById(R.id.commandact);
        button2 =  findViewById(R.id.usergenact);
        logger =  findViewById(R.id.logger);
        cput =  findViewById(R.id.cpu);
        voltaget =  findViewById(R.id.voltage);
        uptimet =  findViewById(R.id.uptime);
        ether1 =  findViewById(R.id.radioButton);
        ether2 =  findViewById(R.id.radioButton2);
        ether3 =  findViewById(R.id.radioButton3);
        ether4 =  findViewById(R.id.radioButton4);
        ether5 =  findViewById(R.id.radioButton5);
        ether6 =  findViewById(R.id.radioButton6);
        ether7 =  findViewById(R.id.radioButton7);
        ether8 =  findViewById(R.id.radioButton8);
        ether9 =  findViewById(R.id.radioButton9);
        ether10 =  findViewById(R.id.radioButton10);
        ether11 =  findViewById(R.id.radioButton11);
        ether12 =  findViewById(R.id.radioButton12);
        ether13 =  findViewById(R.id.radioButton13);

       // MikrotikServer.connect(ServerInformations.IP,ServerInformations.ADMIN,ServerInformations.PASSWORD);

        logger.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {
              loadTask();
               getCpu();
               getUptime();
               getVoltage();
            }
        });

        logger.setOnLongClickListener(new OnLongClickListener() {

            public boolean onLongClick(View view) {
               ledsThread.interrupt();
                reUptadeCpu.stopthis();
               reUptadeUptime.stopthis();
                return true;
            }
        });

        button1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(SelectionActivity.this, CommanderActivity.class));
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(SelectionActivity.this, GenerateUserActivity.class));
            }
        });
    }

    private void loadTask() {

        taskLeds = new TaskLeds();
        Thread thread = new Thread(taskLeds);
        ledsThread = thread;
        thread.start();

    }

    protected void getCpu() {

        ReUptade reUptade = new ReUptade( cput, CPU_COMMAND, "load");
        reUptadeCpu = reUptade;
        reUptade.start();

    }

    protected void getUptime() {

        ReUptade reUptade = new ReUptade( this.uptimet, UPTIME_COMMAND, "uptime");
        reUptadeUptime = reUptade;
        reUptade.start();

    }

    protected void getVoltage() {

        MikrotikServer.execute(VOLTAGE_COMMAND).addExecutionEventListener(new ExecutionEventListener() {
            public void onExecutionSuccess(List<Map<String, String>> mapList) {
               voltaget.setText( mapList.get(0).get("voltage"));

            }

            public void onExecutionFailed(Exception exp) {
               voltaget.setText(exp.getMessage());
            }
        });
    }

    private void checkarsin(RadioButton radioButton, boolean isChecked) {
        if (isChecked) {
            radioButton.setChecked(true);
        } else {
            radioButton.setChecked(false);
        }
    }

    private void turnOnOffLeds() {

        MikrotikServer.execute(RUNNING_TRUE).addExecutionEventListener(new ExecutionEventListener() {
            public void onExecutionSuccess(List<Map<String, String>> mapList) {
                Iterator<Map<String, String>> it = mapList.iterator();
                while (it.hasNext()) {

                    Map<String, String> map = it.next();
                    boolean e1 = map.containsValue("IN");
                    boolean e2 = map.containsValue("LAN2");
                    boolean e3 = map.containsValue("LAN3");
                    boolean e4 = map.containsValue("LAN4");
                    boolean e5 = map.containsValue("LAN5");
                    boolean e6 = map.containsValue("LAN6");
                    boolean e7 = map.containsValue("LAN7");
                    boolean e8 = map.containsValue("LAN8");
                    boolean e9 = map.containsValue("LAN9");
                    boolean e10 = map.containsValue("LANa");
                    boolean e11 = map.containsValue("LANb");
                    boolean e12 = map.containsValue("LANc");
                    boolean e13 = map.containsValue("LANd");
                    Iterator<Map<String, String>> it2 = it;
                    if (e1) {
                        checkarsin(ether1, true);
                    } else {
                        checkarsin(ether1, false);
                    }
                    if (e2) {
                        checkarsin(ether2, true);
                    } else {
                        checkarsin(ether2, false);
                    }
                    if (e3) {

                        checkarsin(ether3, true);
                    } else {

                        checkarsin(ether3, false);
                    }
                    if (e4) {

                        checkarsin(ether4, true);
                    } else {

                        checkarsin(ether4, false);
                    }
                    if (e5) {

                        checkarsin(ether5, true);
                    } else {

                        checkarsin(ether5, false);
                    }
                    if (e6) {

                        checkarsin(ether6, true);
                    } else {

                        checkarsin(ether6, false);
                    }
                    if (e7) {

                        checkarsin(ether7, true);
                    } else {

                        checkarsin(ether7, false);
                    }
                    if (e8) {

                        checkarsin(ether8, true);
                    } else {

                        checkarsin(ether8, false);
                    }
                    if (e9) {

                        checkarsin(ether9, true);
                    } else {

                        checkarsin(ether9, false);
                    }
                    if (e10) {

                        checkarsin(ether10, true);
                    } else {

                        checkarsin(ether10, false);
                    }
                    if (e11) {

                        checkarsin(ether11, true);
                    } else {

                        checkarsin(ether11, false);
                    }
                    if (e12) {

                        checkarsin(ether12, true);
                    } else {

                        checkarsin(ether12, false);
                    }
                    if (e13) {

                        checkarsin(ether13, true);
                    } else {

                        checkarsin(ether13, false);
                    }
                    it = it2;
                }
            }

            public void onExecutionFailed(Exception exp) {
                voltaget.setText(exp.getMessage());
            }
        });
    }
}