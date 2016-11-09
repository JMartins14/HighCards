package com.example.martins.highcards;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by Goncalopinto on 07/11/16.
 */

public class instrucoes extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.instrucoes);
        TextView view = (TextView)findViewById(R.id.instrucoes_maumau);
        view.setText(Html.fromHtml("<p>O jogo tem entre 4 a 8 jogadores que jogam individualmente, onde no início cada jogador recebe 5 cartas.\n"+"Após a distribuição das cartas, a primeira carta a ser descartada na mesa é retirada do topo do monte. Essa carta é considerada como um descarte do primeiro jogador. Após essa jogada, o próximo jogador deve descartar uma carta do mesmo naipe ou mesmo número da carta no topo da mesa.Quando um jogador não possuir uma carta adequada para descartar (não possui nenhuma carta com o mesmo naipe ou número da carta que está na mesa), deve retirar uma carta do baralho, se ainda assim não possuir nenhuma carta para descartar, deve passar a vez.Um jogador pode retirar uma carta do baralho mesmo que este possua alguma carta que possa ser descartada, pode-se retirar do baralho no máximo 1 carta por turno.Um jogador pode retirar uma carta do baralho mesmo que este possua alguma carta que possa ser descartada, pode-se retirar do baralho no máximo 1 carta por turno.Um jogador pode retirar uma carta do baralho mesmo que este possua alguma carta que possa ser descartada, pode-se retirar do baralho no máximo 1 carta por turno.\n"+"Quando um jogador possuir apenas 1 carta na mão deverá avisar os outros jogadores clicando no botão \"mau-mau\".\n"+"O jogador que não avisar aos outros jogadores deverá então retirar 5 cartas do baralho. Se em qualquer momento que não o apropriado, um jogador clicar no botão mau-mau durante o jogo tendo mais de 2 cartas na mão deverá também retirar 5 cartas do baralho.\n"+"O jogo termina quando um jogador conseguir descartar todas as cartas da sua mão, uma a uma.\n"+"Cartas de efeito e punições</p>\n" +
                "        <p>As cartas de efeito são cartas que, quando descartadas, provocam algum efeito a algum jogador ou a certas características do jogo.</p>\n" +
                "        <p>* Ás – faz com que o próximo jogador não jogue a rodada, pulando a sua vez.</p>\n" +
                "        <p>* Dama – quando essa carta é descartada, inverte o sentido de jogo. Se o sentido era horário, o sentido passa a ser anti-horário e vice-versa.</p>\n" +
                "        <p>* Valete – o jogador que a descartou tem o direito de escolher um naipe, sendo que o próximo jogador deve descartar uma carta do naipe escolhido ou outro valete. O valete pode ser descartado mesmo se o naipe e/ou o número da carta no topo da mesa não forem o mesmo (esta regra não é válida quando a carta no topo da mesa for um 7).</p>\n" +
                "        <p>* Sete – o próximo jogador retira duas cartas do baralho e não descarta nenhuma. Porém, se este possuir outro sete para descartar, ele pode optar por não retirar as duas cartas e descartar o sete. Neste caso o sete foi rebatido, e o próximo jogador deverá retirar 4 cartas exceto se ele puder rebater novamente o sete, aumentando em mais 2 cartas a punição para o jogador seguinte, e assim sucessivamente.</p>\n" +
                "        <p>* Nove – quando um nove é descartado, o jogador anterior deve retirar 1 carta do baralho. Diferentemente de um sete, o nove não pode ser rebatido\"</p>"));



        view.setMovementMethod(new ScrollingMovementMethod());

    }

    public void botaovoltar(View view) {
        Intent voltar = new Intent(this,menu_secundario.class);
        startActivity(voltar);
        finish();
    }
}
