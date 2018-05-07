package app.example.martins.highcards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Goncalopinto on 07/11/16.
 */

public class instrucoes extends Activity {
    public String user;
    public String jogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Bundle bundle = getIntent().getExtras();
        user = bundle.getString("username");
        jogo = bundle.getString("Jogo");
        setContentView(R.layout.instrucoes);
        switch (jogo){
            case "MauMau":
                TextView view = (TextView)findViewById(R.id.instrucoes);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenWidth = displaymetrics.widthPixels;
                int screenHeight = displaymetrics.heightPixels;
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.height=screenHeight;
                params.width=screenWidth;
                params.alignWithParent=true;
                params.setMargins((screenWidth/4),(screenHeight/5),(screenWidth/6),(screenHeight/10));
                view.setLayoutParams(params);
                view.setText(Html.fromHtml("<h1>MauMau</h1>"+
                        "<p>O jogo tem 4 jogadores que, inicialmente, recebem 7 cartas.</p>"+
                        "<p>É retirada uma carta do baralho, colocada na mesa e o jogo começa!</p>"+
                        "<p>O próximo jogador deve jogar uma carta do mesmo naipe ou mesmo número da carta no topo da mesa.</p>"+
                        "<p>Quando um jogador não possuir uma carta adequada para jogar, deve retirar uma carta do baralho e assim passar a sua vez.</p>"+
                        "<p>Não se é obrigado a jogar e pode-se retirar do baralho até uma carta por turno.</p>"+
                        "<p>Um jogador acaba o jogo quando um conseguir descartar todas as cartas da sua mão, uma a uma.</p>"+
                        "<p>Um jogo termina quando só houver um jogador a jogar. </p>"
                ));
                view.setMovementMethod(new ScrollingMovementMethod());
                break;
            case "MauMau2":
                view = (TextView)findViewById(R.id.instrucoes);
                displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                screenWidth = displaymetrics.widthPixels;
                screenHeight = displaymetrics.heightPixels;
                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.height=screenHeight;
                params.width=screenWidth;
                params.alignWithParent=true;
                params.setMargins((screenWidth/4),(screenHeight/5),(screenWidth/6),(screenHeight/9));
                view.setLayoutParams(params);
                view.setText(Html.fromHtml("<h1>MauMau</h1>"+
                        "<p>As cartas de efeito são cartas que, quando descartadas, provocam algum efeito a algum jogador ou a certas características do jogo.</p>" +
                        "<p>* Ás – faz com que o próximo jogador não jogue a rodada, pulando a sua vez.</p>" +
                        "<p>* Dama – quando essa carta é descartada, inverte o sentido de jogo. Se o sentido era horário, o sentido passa a ser anti-horário e vice-versa.</p>" +
                        "<p>* Valete – o jogador que a descartou tem o direito de escolher um naipe, sendo que o próximo jogador deve descartar uma carta do naipe escolhido ou outro valete (esta regra não é válida quando a carta no topo da mesa for um 7).</p>" +
                        "<p>* Sete – o próximo jogador retira duas cartas do baralho e não descarta nenhuma. Porém, se este possuir outro sete para descartar, ele pode optar por não retirar as duas cartas e descartar o sete. Neste caso o sete foi rebatido, e o próximo jogador deverá retirar 4 cartas exceto se ele puder rebater novamente o sete, aumentando em mais 2 cartas a punição para o jogador seguinte, e assim sucessivamente.</p>" +
                        "<p>* Nove – quando um nove é descartado, o jogador seguinte deve retirar 1 carta do baralho. Diferentemente de um sete, o nove não pode ser rebatido</p>"));
                view.setMovementMethod(new ScrollingMovementMethod());
                break;
            case "Desconfia":
                view = (TextView)findViewById(R.id.instrucoes);
                displaymetrics = new DisplayMetrics();
                this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                screenWidth = displaymetrics.widthPixels;
                screenHeight = displaymetrics.heightPixels;
                params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
                params.height=screenHeight;
                params.width=screenWidth;
                params.alignWithParent=true;
                params.setMargins((screenWidth/4),(screenHeight/5),(screenWidth/6),(screenHeight/9));
                view.setLayoutParams(params);
                view.setText(Html.fromHtml("<h1>Desconfia</h1>\n" +
                        "<p>No Desconfia, o baralho é repartido por todos os jogadores. Você começa, podendo estar uma ou mais cartas viradas para baixo e informa os outros jogadores de que carta/as se trata/am. No entanto, pode estar a mentir. Os outros jogadores, à vez, têm de jogar cartas com o mesmo número.</p>"
                        +       "<p>Quando um jogador acha que outro está a mentir sobre a carta que pôs, pode desconfiar. Desconfia, e levanta a carta ou cartas que o outro jogador colocou. Se este tiver mentido, leva todas as cartas do monte. Se estiver a dizer a verdade, o jogador que desconfiou é que leva o monte.</p>"
                        +       "<p>Na nova jogada, joga primeiro quem ganhou: o desconfiado, se tiver dito a verdade, ou o que desconfiou, se o outro tiver mentido.</p>"
                        +       "<p>O jogo termina quando todos os jogadores ficarem sem cartas.</p>")
                );
                view.setMovementMethod(new ScrollingMovementMethod());
                break;
            default:
                break;
        }

    }

    public void botaovoltar(View view) {
        Intent voltar = new Intent(this,menu_secundario.class);
        voltar.putExtra("username",user);
        voltar.putExtra("Jogo",jogo);
        startActivity(voltar);
        finish();
    }
    public void botaoNext(View view) {
        switch (jogo){
            case "MauMau":
                Intent voltar = new Intent(this,instrucoes.class);
                voltar.putExtra("username","Name");
                voltar.putExtra("Jogo","MauMau2");
                startActivity(voltar);
                finish();
                break;
            case "Desconfia":
                voltar = new Intent(this,instrucoes.class);
                voltar.putExtra("username",user);
                voltar.putExtra("Jogo","Desconfia");
                startActivity(voltar);
                finish();
                break;
            default:
                voltar = new Intent(this,instrucoes.class);
                voltar.putExtra("username",user);
                voltar.putExtra("Jogo","MauMau2");
                startActivity(voltar);
                finish();
                break;
        }
    }
    public void botaoAnterior(View view){
        switch (jogo) {
            case "MauMau":
                Intent voltar = new Intent(this, instrucoes.class);
                voltar.putExtra("username", user);
                voltar.putExtra("Jogo", "MauMau");
                startActivity(voltar);
                finish();
                break;
            case "Desconfia":
                voltar = new Intent(this, instrucoes.class);
                voltar.putExtra("username", user);
                voltar.putExtra("Jogo", "Desconfia");
                startActivity(voltar);
                finish();
                break;
            default:
                voltar = new Intent(this, instrucoes.class);
                voltar.putExtra("username", user);
                voltar.putExtra("Jogo", "MauMau");
                startActivity(voltar);
                finish();
                break;
        }
    }
}
