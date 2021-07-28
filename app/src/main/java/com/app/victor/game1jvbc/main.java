package com.app.victor.game1jvbc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {

    private TextView txtPuntuacion, txtComenzar;
    private ImageView imgHongoN, imgHongoR, imgPersonaje, imgBomba, imgVida, imgMisil;
    private LinearLayout lyVidas;

    //tamaño
    private int frameAltura;
    private int tamaño_personaje;
    private int pantallaAncho;
    private int panatallaAlto;
    private int puntucion, vidas;

    //posicion de la imagen
    private int personaje_y;
    private int hongonX, hongonY, bombaX, bombaY, vidaX, vidaY, hongorX, hongorY, misilX, misilY;
    private int personajeVelocidad;
    private int ongorVelocidad;
    private int ongonVelocidad;
    private int bombaVelocidad;
    private int vidaVelocidad;
    private int misilVelocidad;

    private float hr = 65F, hn = 40F, b = 50F, m = 35F, v = 40F;

    //inicializar clases
    private Handler handler = new Handler(); // en una clase que sirve para crear y manejar subprosesos
    private Timer timer; //
    private sonidos sonido;

    //pantalla tamaño
    WindowManager wm;
    Display disp;
    Point size;

    AlertDialog ad;

    //comprobacion de estado
    private  boolean action_flg = false;
    private boolean start_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtComenzar = (TextView)findViewById(R.id.txt_comenzar);
        txtPuntuacion = (TextView)findViewById(R.id.txt_puntuacion);
        lyVidas = findViewById(R.id.lyVidas);


        imgHongoR = (ImageView)findViewById(R.id.imgSemilla);
        imgHongoN = (ImageView)findViewById(R.id.img_ongo_naranja);
        imgBomba = (ImageView)findViewById(R.id.img_boma);
        imgPersonaje = (ImageView)findViewById(R.id.img_personaje);
        imgVida = (ImageView)findViewById(R.id.img_vida);
        imgMisil = (ImageView)findViewById(R.id.img_misil);

        sonido = new sonidos(this);
        timer = new Timer();

        //traer el tamaño de la pantalla
        wm = getWindowManager();
        disp = wm.getDefaultDisplay();
        size = new Point();
        disp.getSize(size);

        pantallaAncho = size.x;
        panatallaAlto = size.y;
        // Ahora
        //Pantalla con tamaño horizontal: 768 alto: 1184
        // Velocidad personaje:20 hongo Rojo: 12 hongo Naranja 18; misil: 25, bomba: 13 vida:18

        personajeVelocidad = Math.round(panatallaAlto / 60F);

        imgHongoR.setX(-80);
        imgHongoR.setY(-80);
        imgHongoN.setX(-80);
        imgHongoN.setY(-80);
        imgVida.setX(-80);
        imgVida.setY(-80);
        imgBomba.setX(-80);
        imgBomba.setY(-80);
        imgMisil.setX(-80);
        imgMisil.setY(-80);

        modalMenu();
    }

    public void cambiarPosicion(){
        ongonVelocidad = Math.round(pantallaAncho / hn);
        ongorVelocidad = Math.round(pantallaAncho / hr);
        bombaVelocidad = Math.round(pantallaAncho / b);
        misilVelocidad = Math.round(pantallaAncho / m);
        vidaVelocidad = Math.round(pantallaAncho / v);

        golpeoObjetos();
        // ongo rojo
        hongorX -= ongorVelocidad; // veocidad de la figura
        if(hongorX < 0){
            hongorX = pantallaAncho + 60; // velocidad al que se desplaza la el objeto en la posicion x
            hongorY = (int) Math.floor(Math.random() * (frameAltura - imgHongoR.getHeight())); // en que posicion se encontrara el objeto en y
        }
        imgHongoR.setX(hongorX);
        imgHongoR.setY(hongorY);

        // bomba
        bombaX -= bombaVelocidad;
        if(bombaX < 0){
            bombaX = pantallaAncho + 10;
            bombaY = (int) Math.floor(Math.random() * (frameAltura - imgBomba.getHeight()));
        }
        imgBomba.setY(bombaY);
        imgBomba.setX(bombaX);

        // ongo naranja
        hongonX -= ongonVelocidad;
        if(hongonX < 0){
            hongonX = pantallaAncho + 5000;
            hongonY = (int) Math.floor(Math.random() * (frameAltura - imgHongoN.getHeight()));
        }
        imgHongoN.setX(hongonX);
        imgHongoN.setY(hongonY);

        // dinamita
        misilX -= misilVelocidad;
        if(misilX < 0){
            misilX = pantallaAncho + 500;
            misilY = (int) Math.floor(Math.random() * (frameAltura - imgMisil.getHeight()));
        }
        imgMisil.setX(misilX);
        imgMisil.setY(misilY);

        // vida
        vidaX -= vidaVelocidad;
        if(vidaX < 0){
            vidaX = pantallaAncho + 7000; // velocidad de
            vidaY = (int) Math.floor(Math.random() * (frameAltura - imgVida.getHeight()));
        }
        imgVida.setX(vidaX);
        imgVida.setY(vidaY);
        ///////////////////////////////////////

        if(action_flg){
            personaje_y -= personajeVelocidad;
        }else{
            // Liberando
            personaje_y += personajeVelocidad;
        }
        //fijar la posicion del personaje
        if(personaje_y < 0) personaje_y = 0;
        if(personaje_y > frameAltura - tamaño_personaje) personaje_y = frameAltura - tamaño_personaje;

        imgPersonaje.setY(personaje_y);
        txtPuntuacion.setText("Puntuacion: "+puntucion);
        generarVidas(vidas);
    }

    private void generarVidas(int vd){
        lyVidas.removeAllViews();
        for (int i = 0; i<vd; i++){
            ImageView imageView = new ImageView(main.this);
            imageView.setImageResource(R.drawable.hearts);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(33, 33);
            params.setMargins(3,0,0,0);
            imageView.setLayoutParams(params);
            imageView.setId(i);
            lyVidas.addView(imageView);
        }
    }
    public void golpeoObjetos(){
        // Si el centro de la pelota está en la caja, cuenta como un golpe.
        // ongo rojo
        int hongorCentroX = hongorX + imgHongoR.getWidth() / 2;
        int hongorCentroY = hongorY + imgHongoR.getHeight() / 2;

        if(0 <= hongorCentroX && hongorCentroX <= tamaño_personaje && personaje_y <= hongorCentroY && hongorCentroY <= personaje_y + tamaño_personaje){
            hongorX = -10;
            puntucion += 10;
            sonido.playGolpeSonido();
        }
        int hongonCentroX = hongonX + imgHongoN.getWidth() / 2;
        int hongonCentroY = hongonY + imgHongoN.getHeight() / 2;

        if(0 <= hongonCentroX && hongonCentroX <= tamaño_personaje && personaje_y <= hongonCentroY && hongonCentroY <= personaje_y + tamaño_personaje){
            hongonX = -10;
            puntucion +=30;
            sonido.playGolpeSonido();
        }

        int vidaCentroX = vidaX + imgVida.getWidth() / 2;
        int vidaCentroY = vidaY + imgVida.getHeight() / 2;

        if(0 <= vidaCentroX && vidaCentroX <= tamaño_personaje && personaje_y <= vidaCentroY && vidaCentroY <= personaje_y + tamaño_personaje){
            vidaX = -10;
            vidas ++;
            sonido.playGolpeSonido();
        }

        int bombaCentroX = bombaX + imgBomba.getWidth() / 2;
        int bombaCentroY = bombaY + imgBomba.getHeight() / 2;

        if(0 <= bombaCentroX && bombaCentroX <= tamaño_personaje && personaje_y <= bombaCentroY && bombaCentroY <= personaje_y + tamaño_personaje){
            bombaX = -10;
            if(vidas<=0){
                fallo(puntucion);
            }else{
                sonido.playMenosVidaSonido();
                vidas--;
            }
        }

        int misilCentroX = misilX + imgMisil.getWidth() / 2;
        int misilCentroY = misilY + imgMisil.getHeight() / 2;

        if(0 <= misilCentroX && misilCentroX <= tamaño_personaje && personaje_y <= misilCentroY && misilCentroY <= personaje_y + tamaño_personaje){
            misilX = -10;
            if(vidas<=0){
               fallo(puntucion);
            }else{
                sonido.playMenosVidaSonido();
                vidas--;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent me){
        if(start_flg == false){
            start_flg = true;

            //¿Por qué obtener la altura del personaje y la altura del cuadro aquí?
            //porque la interfaz de usuario no se ha configurado en la pantalla en OnCreate
            FrameLayout frame = (FrameLayout)findViewById(R.id.frame);
            frameAltura = frame.getHeight(); //tomamos el valos mas alto de fragmento

            personaje_y = (int)imgPersonaje.getY(); //El personaje es un cuadrado (la altura y el ancho son los mismos)
            tamaño_personaje = imgPersonaje.getHeight();

            txtComenzar.setVisibility(View.INVISIBLE);

            TimerTask tarea = new TimerTask() { // se ejecurara cada sierto tiempo
                @Override
                public void run() {
                    //aqui se ejecuta las tareas
                    Runnable runnable = new Runnable() { // creamos un hilo
                        @Override
                        public void run() {
                            cambiarPosicion(); // corremos el metodo
                        }
                    };
                    handler.post(runnable); // ejecutamos el hilo
                }
            };
            timer.schedule(tarea, 0, 20); //se llama cada 20 milisegundo

            //resumiendo el codigo seria de esta manera mas facil
            /*timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() { // ejecutamos en un hilo
                        @Override
                        public void run() {
                            cambiarPosicion();
                        }
                    });
                }
            },0,23);*/

            TimerTask tarea2 = new TimerTask() { // se ejecurara cada sierto tiempo
                @Override
                public void run() {
                    //aqui se ejecuta la tarea que sera cada 10000 milisegundo ingrementa los valores en 1
                    hr = (float) (hr-0.5); hn=(float) (hr-0.5); b=(float) (hr-0.5); m=(float) (hr-0.5); v=(float) (hr-0.5);
                }
            };
            timer.schedule(tarea2, 0, 1000);

        }else if(me.getAction() == MotionEvent.ACTION_DOWN){
            if(!perdio)imgPersonaje.setImageResource(R.drawable.personaje1);
            action_flg = true;
        }else if( me.getAction()== MotionEvent.ACTION_UP){
            if(!perdio)imgPersonaje.setImageResource(R.drawable.personaje2);
           action_flg = false;
        }
        imgPersonaje.setY(personaje_y);
        return true;
    }
    boolean perdio = false;
    private void modalPerdio(int pt){
        LayoutInflater inf = getLayoutInflater();
        final View vista = inf.inflate(R.layout.modal_fin_game, null);

        final Button btnOtraVez = (Button) vista.findViewById(R.id.btnOtraVez);
        final TextView txtPuntuacionModal = (TextView) vista.findViewById(R.id.txtPuntuaionModal);
        final TextView txtPuntuacionAlta = (TextView) vista.findViewById(R.id.txtPuntuacionAlta);

        SharedPreferences preferencias = getSharedPreferences("puntaje", Context.MODE_PRIVATE);
        int puntaje_alto=preferencias.getInt("valor",0);

        if(pt>puntaje_alto){
            txtPuntuacionModal.setText("¡NUEVO RECORD!\n"+pt);
            txtPuntuacionModal.setTextColor(Color.parseColor("#e3ef4923"));
            guardarPreferencias(pt);
        }else{
            txtPuntuacionModal.setText(String.valueOf(pt));
        }
        txtPuntuacionAlta.setText("Puntuacion mas Alta: "+puntaje_alto);

        final AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setView(vista);

        buil.setCancelable(false);
        ad = buil.create();


        btnOtraVez.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(getIntent());
            }
        });
        ad.show();
    }
    private void modalMenu(){
        LayoutInflater inf = getLayoutInflater();
        final View vista = inf.inflate(R.layout.modal_menu, null);

        final Button btnIniciar = (Button) vista.findViewById(R.id.btnJugar);
        final TextView txtPuntuacionAlta = (TextView) vista.findViewById(R.id.txtPuntuacionAltaMenu);
        SharedPreferences preferencias = getSharedPreferences("puntaje", Context.MODE_PRIVATE);
        int puntaje_alto=preferencias.getInt("valor",0);

        txtPuntuacionAlta.setText("Puntuacion mas Alta: "+puntaje_alto);

        final AlertDialog.Builder buil = new AlertDialog.Builder(this);
        buil.setView(vista);

        buil.setCancelable(false);

        ad = buil.create();

        btnIniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ad.dismiss();
            }
        });
        ad.show();
    }
    private void guardarPreferencias(int valor) {

        SharedPreferences preferencias = getSharedPreferences("puntaje", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putInt("valor", valor);
        editor.commit();
    }

    private void fallo(final int pt) {
        perdio = true;
        timer.cancel();
        timer = null;
        sonido.playFinSonido();

        imgPersonaje.getLayoutParams().width = 155; // tamaño en x
        imgPersonaje.getLayoutParams().height = 155; // tamaño en y del personaje
        imgPersonaje.setAdjustViewBounds(true);
        imgPersonaje.setImageResource(R.drawable.explosion);
       new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               modalPerdio(pt);
           }
       },2000); //despues de 5 segundos se visualiza el moal de puntajes
    }


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(timer != null){
            timer.cancel();
            wm = getWindowManager();
            disp = wm.getDefaultDisplay();
            size = new Point();
            disp.getSize(size);

            start_flg =false;

            pantallaAncho = size.x;
            panatallaAlto = size.y;
            txtComenzar.setVisibility(View.VISIBLE);
            txtComenzar.setText("Toca para continuar!");

            personaje_y = (int)imgPersonaje.getY();

            imgPersonaje.setY(personaje_y);
            timer = new Timer();
        }
    }

}
