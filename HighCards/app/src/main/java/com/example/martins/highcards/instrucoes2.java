package com.example.martins.highcards;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Goncalopinto on 09/11/16.
 */

public class instrucoes2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.instrucoes2);
        TextView view = (TextView)findViewById(R.id.instrucoes_maumau);
        view.setText(Html.fromHtml("Cartas de efeito e punições<p>As cartas de efeito são cartas que, quando descartadas, provocam algum efeito a algum jogador ou a certas características do jogo.</p>\n" +
                "        <p>* Ás – faz com que o próximo jogador não jogue a rodada, pulando a sua vez.</p>\n" +
                "        <p>* Dama – quando essa carta é descartada, inverte o sentido de jogo. Se o sentido era horário, o sentido passa a ser anti-horário e vice-versa.</p>\n" +
                "        <p>* Valete – o jogador que a descartou tem o direito de escolher um naipe, sendo que o próximo jogador deve descartar uma carta do naipe escolhido ou outro valete. O valete pode ser descartado mesmo se o naipe e/ou o número da carta no topo da mesa não forem o mesmo (esta regra não é válida quando a carta no topo da mesa for um 7).</p>\n" +
                "        <p>* Sete – o próximo jogador retira duas cartas do baralho e não descarta nenhuma. Porém, se este possuir outro sete para descartar, ele pode optar por não retirar as duas cartas e descartar o sete. Neste caso o sete foi rebatido, e o próximo jogador deverá retirar 4 cartas exceto se ele puder rebater novamente o sete, aumentando em mais 2 cartas a punição para o jogador seguinte, e assim sucessivamente.</p>\n" +
                "        <p>* Nove – quando um nove é descartado, o jogador anterior deve retirar 1 carta do baralho. Diferentemente de um sete, o nove não pode ser rebatido\"</p>"));

        view.setMovementMethod(new ScrollingMovementMethod());

    }

}

