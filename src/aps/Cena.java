package aps;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author daniel
 */
public class Cena implements GLEventListener, KeyListener{
    private TextRenderer textRenderer;
    private boolean abertura = true;
    private float eixoX = 0;
    private boolean comecou = false;
    private boolean andaDireita = true;
    private boolean andaEsquerda = false;
    private boolean andaCima = true;
    private boolean andaBaixo = false;
    private float eixoXbola = 0f;
    private float eixoYbola = -88f;
    private float limiteXdir = 91f;
    private float limiteXesq = -91f;
    private float limiteYcima = 91f;
    private int score = 0;
    private int vida = 5;
    private boolean pause = false;
    private int tela = 1;
    private boolean bolaFora = true;
    private int aumentaScore = 10;
     

    
    @Override
    public void init(GLAutoDrawable drawable) {
        //dados iniciais da cena
        GL2 gl = drawable.getGL().getGL2();
        //habilita o buffer de profundidade
        gl.glEnable(GL2.GL_DEPTH_TEST);
               
        textRenderer = new TextRenderer(new Font("Arial", Font.PLAIN, 30));
    }

    @Override
    public void display(GLAutoDrawable drawable) {  
        //obtem o contexto Opengl
        GL2 gl = drawable.getGL().getGL2();    
        GLUT glut = new GLUT(); //objeto da biblioteca GLUT        
        //define a cor da janela (R, G, G, alpha)
        gl.glClearColor(0, 0, 0, 1);        
        //limpa a janela com a cor especificada
        //limpa o buffer de profundidade
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);       
        gl.glLoadIdentity(); //lê a matriz identidade
        
        /*
            desenho da cena        
        *
        */
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);;
        

        
        //texto inicial
        
        
        if(abertura){
            desenhaFundoAbertura(gl);
            desenhaTexto(300, 700, Color.WHITE, "Bem vindo ao Pong");
            desenhaTexto(90, 500, Color.WHITE, "Intruções de jogo:");
            desenhaTexto(90, 450, Color.WHITE, "- Use as setas direita e esquerda para movimentar");
            desenhaTexto(90, 400, Color.WHITE, "- Aperte a tecla 'p' para pausar");
            desenhaTexto(90, 350, Color.WHITE, "- Aperte espaço para lançar a bolinha");
            desenhaTexto(90, 300, Color.WHITE, "- Aperte Enter para iniciar o jogo");
            desenhaTexto(90, 250, Color.WHITE, "- Aperte a tecla 's' voltar para tela inicial");
            desenhaTexto(90, 200, Color.WHITE, "- Aperte ESC para encerrar o programa");
        }else{
            if(vida == 0){
            abertura = true;
            eixoX = 0;
            comecou = false;
            andaDireita = true;
            andaEsquerda = false;
            andaCima = true;
            andaBaixo = false;
            eixoXbola = 0f;
            eixoYbola = -88f;
            score = 0;
            vida = 5;
            tela = 1;
            bolaFora = true;
            }
            if(pause == false){
                if(tela==1){
                    //primeira tela
                    desenhaFundo(gl);
                    desenhaTexto(300,750, Color.WHITE, "SCORE: "+score);
                    desenhaTexto(500,750, Color.WHITE, "Fase "+tela);
                    
                    gl.glPushMatrix();
                        gl.glTranslatef(-38f, 94f, 0);
                        vida(gl, glut,vida);
                    gl.glPopMatrix();
                    
                    gl.glPushMatrix();
                        gl.glTranslatef(eixoX , 0, 0 );
                        barra(gl);
                    gl.glPopMatrix();
                    
                    if(comecou){
                        bolaFora=false;
                        gl.glPushMatrix();
                            gl.glTranslatef(eixoXbola , eixoYbola, 0 );
                            bola(gl, glut);
                        gl.glPopMatrix();
                        if(eixoXbola<=limiteXdir && andaDireita){
                            eixoXbola = eixoXbola + 1.3f;
                        }else{
                            andaDireita = false;
                            andaEsquerda = true;
                        }
                        if(eixoXbola>=limiteXesq && andaEsquerda){
                            eixoXbola = eixoXbola -1.3f;
                        }else{
                            andaDireita = true;
                            andaEsquerda = false;
                        }

                        if(eixoYbola<=limiteYcima && andaCima){
                            eixoYbola = eixoYbola + 2f;
                        }else{
                            andaCima = false;
                            andaBaixo = true;
                        }

                        if(eixoYbola >= -105f && andaBaixo){
                            eixoYbola = eixoYbola - 2f;                 
                        if(eixoYbola <= -90f && eixoXbola <= eixoX && eixoXbola >= eixoX-19){                    
                            andaCima = true;
                            andaBaixo = false;
                            andaEsquerda = true;
                            andaDireita = false;
                            score = score + aumentaScore;
                        }
                        if(eixoYbola <= -90f && eixoXbola <= eixoX+19 && eixoXbola >= eixoX){                    
                            andaCima = true;
                            andaBaixo = false;
                            andaEsquerda = false;
                            andaDireita = true;
                            score = score + aumentaScore;
                        }
                        if(eixoYbola <= -105f){
                            vida = vida -1;
                            comecou = false;
                            bolaFora = true;
                            andaCima = true;
                            andaBaixo = false;
                            andaEsquerda = false;
                            andaDireita = true;
                        }             
                    }
                    if(score == 200){
                        tela = 2;
                    }
                }
            }
        }else{
                desenhaTexto(300,750, Color.WHITE, "SCORE: "+score);
                desenhaTexto(500,750, Color.WHITE, "Fase "+tela);
                desenhaTexto(300, 500, Color.WHITE, "PAUSA");
                gl.glPushMatrix();
                    gl.glTranslatef(-38f, 94f, 0);
                    vida(gl, glut,vida);
                gl.glPopMatrix();
        }
        if(pause == false){
            if (tela ==2){
                    //segunda tela
                    desenhaFundo(gl);
                    desenhaTexto(300,750, Color.WHITE, "SCORE: "+score);
                    desenhaTexto(500,750, Color.WHITE, "Fase "+tela);
                    
                    gl.glPushMatrix();
                        gl.glTranslatef(-38f, 94f, 0);
                        vida(gl, glut,vida);
                    gl.glPopMatrix();                    
                                        
                    gl.glPushMatrix();
                        barreira(gl);
                    gl.glPopMatrix();
                    
                    gl.glPushMatrix();
                        gl.glTranslatef(eixoX , 0, 0 );
                        barra(gl);
                    gl.glPopMatrix();

                    if(comecou){
                        bolaFora=false;
                        gl.glPushMatrix();
                            gl.glTranslatef(eixoXbola , eixoYbola, 0 );
                            bola(gl, glut);
                        gl.glPopMatrix();
                        if(eixoXbola<=limiteXdir && andaDireita){
                            eixoXbola = eixoXbola + 1.9f;
                        }else{
                            andaDireita = false;
                            andaEsquerda = true;
                        }
                        if(eixoXbola>=limiteXesq && andaEsquerda){
                            eixoXbola = eixoXbola -1.9f;
                        }else{
                            andaDireita = true;
                            andaEsquerda = false;
                        }

                        if(eixoYbola<=limiteYcima && andaCima){
                            eixoYbola = eixoYbola + 2.5f;
                        }else{
                            andaCima = false;
                            andaBaixo = true;
                        }

                        if(eixoXbola>=-26 && eixoXbola<=26 && eixoYbola >= 9 && eixoYbola<=27 && andaCima && andaDireita){
                            andaDireita = false;
                            andaEsquerda = true;
                        }
                        
                        if(eixoXbola>=-26 && eixoXbola<=26 && eixoYbola >= 9 && eixoYbola<=27 && andaCima && andaEsquerda){
                            andaDireita = true;
                            andaEsquerda = false;
                        }                      
                                                
                        if(eixoXbola>=-25 && eixoXbola<=25 && eixoYbola >= 7 && eixoYbola<=12 && andaCima && andaDireita){
                            andaCima = false;
                            andaBaixo = true;
                        }
                        
                        if(eixoXbola>=-25 && eixoXbola<=25 && eixoYbola >= 7 && eixoYbola<=12 && andaCima && andaEsquerda){
                            andaCima = false;
                            andaBaixo = true;
                        }
                        
                        if(eixoXbola>=-25 && eixoXbola<=25 && eixoYbola <= 27 && eixoYbola>=15 && andaBaixo && andaEsquerda){
                            andaCima = true;
                            andaBaixo = false;
                        }
                        
                        if(eixoXbola>=-25 && eixoXbola<=25 && eixoYbola <= 27 && eixoYbola>=15 && andaBaixo && andaDireita){
                            andaCima = true;
                            andaBaixo = false;
                        }
                        
                        if(eixoXbola>=-26 && eixoXbola<=26 && eixoYbola >= 10 && eixoYbola<=26 && andaBaixo && andaDireita){
                            andaDireita = false;
                            andaEsquerda = true;
                        }
                        
                        if(eixoXbola>=-26 && eixoXbola<=26 && eixoYbola >= 10 && eixoYbola<=26 && andaBaixo && andaEsquerda){
                            andaDireita = true;
                            andaEsquerda = false;
                        }
                                                
                        if(eixoYbola >= -105f && andaBaixo){
                            eixoYbola = eixoYbola - 3f;                 
                            if(eixoYbola <= -90f && eixoXbola <= eixoX && eixoXbola >= eixoX-19){                    
                                andaCima = true;
                                andaBaixo = false;
                                andaEsquerda = true;
                                andaDireita = false;
                                score = score + aumentaScore;
                            }
                            if(eixoYbola <= -90f && eixoXbola <= eixoX+19 && eixoXbola >= eixoX){                    
                                andaCima = true;
                                andaBaixo = false;
                                andaEsquerda = false;
                                andaDireita = true;
                                score = score + aumentaScore;
                            }
                            if(eixoYbola <= -105f){
                                vida = vida -1;
                                comecou = false;
                                bolaFora = true;
                                andaCima = true;
                                andaBaixo = false;
                                andaEsquerda = false;
                                andaDireita = true;
                            }             
                        }
                    }
                }
            }else{
                    desenhaTexto(300,750, Color.WHITE, "SCORE: "+score);
                    desenhaTexto(500,750, Color.WHITE, "Fase "+tela);
                    desenhaTexto(300, 500, Color.WHITE, "PAUSA");
                    gl.glPushMatrix();
                        gl.glTranslatef(-38f, 94f, 0);
                        vida(gl, glut,vida);
                    gl.glPopMatrix();
            }
        }       

        gl.glFlush();   


    }
    
    
    public void desenhaFundoAbertura(GL2 gl){
        gl.glColor3f(.5f,.2f,.2f);
        gl.glBegin(GL2.GL_TRIANGLES);
            gl.glVertex2f(-100f, -100f);
            gl.glVertex2f(-100f, 100f);
            gl.glVertex2f(100f, 100f);
        gl.glEnd();
    }


    
    public void barra(GL2 gl){
        gl.glColor3f(.9f,.9f,.9f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(-18f, -98f);
            gl.glVertex2f(-18f, -93f);
            gl.glVertex2f(18f, -93f);
            gl.glVertex2f(18f, -98f);
        gl.glEnd(); 
    }
    
    public void desenhaFundo(GL2 gl){
        gl.glColor3f(.5f,.5f,.5f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(-100f, -100f);
            gl.glVertex2f(-95f, -100f);
            gl.glVertex2f(-95f, 100f);
            gl.glVertex2f(-100f, 100f);
        gl.glEnd(); 
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(-100f, 100f);
            gl.glVertex2f(-100f, 95f);
            gl.glVertex2f(100f, 95f);
            gl.glVertex2f(100f, 100f);
        gl.glEnd(); 
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(100f, 100f);
            gl.glVertex2f(95f, 100f);
            gl.glVertex2f(95f, -100f);
            gl.glVertex2f(100f, -100f);
        gl.glEnd(); 
    }
    
    public void desenhaTexto(int xPosicao, int yPosicao, Color cor, String frase) {
        //Retorna a largura e altura da janela
        textRenderer.beginRendering(Renderer.screenWidth, Renderer.screenHeight);       
        textRenderer.setColor(cor);
        textRenderer.draw(frase, xPosicao, yPosicao);
        textRenderer.endRendering();
    }
    
    public void bola(GL2 gl, GLUT glut){
        ligaLuz(gl);
        gl.glColor3f(1f, 0f, 0f);
        glut.glutSolidSphere(4,15,15);
        desligaluz(gl);
    }
    
    public void vida(GL2 gl, GLUT glut, int vida){
            ligaLuz(gl);
            gl.glColor3f(1f, 0f, 1f);
            gl.glRotatef(90, 1 , 0 , 0);
            gl.glScalef(0.13f, 0.5f, 0.15f);
            if(vida ==5){
            gl.glPushMatrix();
                gl.glTranslatef(0f , 0, 00f );
                glut.glutSolidCone(50, 50, 13, 5);
            gl.glPopMatrix();
            }
            if(vida >=4){
            gl.glPushMatrix();
                gl.glTranslatef(-90f , 0, 00f );
                glut.glutSolidCone(50, 50, 13, 5);
            gl.glPopMatrix();
            }
            if(vida >=3){
            gl.glPushMatrix();
                gl.glTranslatef(-180f , 0, 00f );
                glut.glutSolidCone(50, 50, 13, 5);
            gl.glPopMatrix();
            }
            if(vida >=2){
            gl.glPushMatrix();
                gl.glTranslatef(-270f , 0, 00f );
                glut.glutSolidCone(50, 50, 13, 5);
            gl.glPopMatrix();
            }
            if(vida >=1){
            gl.glPushMatrix();
                gl.glTranslatef(-360f , 0, 00f );
                glut.glutSolidCone(50, 50, 13, 5);
            gl.glPopMatrix();
            }
            desligaluz(gl);
    }
    
    public void barreira (GL2 gl){
        gl.glColor3f(.5f,.4f,.4f);
        gl.glBegin(GL2.GL_QUADS);
            gl.glVertex2f(25f, 25f);
            gl.glVertex2f(-25f, 25f);
            gl.glVertex2f(-25f, 10f);
            gl.glVertex2f(25f, 10f);
        gl.glEnd(); 
    }
    
    public void ligaLuz(GL2 gl) {        
        float luzDifusa[] = {0.9f, 0.5f, 0.2f, 1.0f}; //cor
        float posicaoLuz[] = {-30.0f, 0.0f, 100.0f, 1.0f}; //1.0 pontual

        gl.glEnable(GL2.GL_COLOR_MATERIAL);         
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, luzDifusa, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posicaoLuz, 0);  
        gl.glShadeModel(GL2.GL_SMOOTH);
    }

    public void desligaluz(GL2 gl) {
        //desabilita o ponto de luz
        gl.glDisable(GL2.GL_LIGHT0);
        //desliga a iluminacao
        gl.glDisable(GL2.GL_LIGHTING);
    }


    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {    
        //obtem o contexto grafico Opengl
        GL2 gl = drawable.getGL().getGL2();        
        //ativa a matriz de projeção
        gl.glMatrixMode(GL2.GL_PROJECTION);      
        gl.glLoadIdentity(); //lê a matriz identidade
        //projeção ortogonal (xMin, xMax, yMin, yMax, zMin, zMax)
        gl.glOrtho(-100,100,-100,100,-100,100);
        //ativa a matriz de modelagem
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        System.out.println("Reshape: " + width + ", " + height);
    }  
       
    @Override
    public void dispose(GLAutoDrawable drawable) {}        

    @Override
    public void keyPressed(KeyEvent e) {         
        switch(e.getKeyCode()){
            case KeyEvent.VK_ENTER:
                abertura=false;
                break;            
            case KeyEvent.VK_RIGHT:
                if(eixoX <= 72 && pause==false){
                eixoX += 11f;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(eixoX >= -72 && pause == false){
                eixoX -= 11f;
                }
                break;
            case KeyEvent.VK_SPACE:
                if(abertura == false && bolaFora == true){
                    comecou = true;
                    eixoXbola = eixoX;
                    eixoYbola = -88f;
                }
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;
        }
        switch (e.getKeyChar()) {
            case 'p':
                if(pause==false){
                    pause = true;
                }else{
                    pause = false;
                }
                break;
            case 's':
                if(abertura == false){
                vida = 0;                    
                }

                break;
        }

        }
                    
    @Override
    public void keyReleased(KeyEvent e) {
    }

}


