package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;
import java.util.List;
import java.util.ArrayList;


public class PantallaJuego implements Screen {

	private SpaceNavigation game;
	private OrthographicCamera camera;	
	private SpriteBatch batch;
	private Texture fondo;
	private Sound explosionSound;
	private Music gameMusic;
	private Sound pauseSound;
	private int score;
	private int ronda;
	private int velXAsteroides; 
	private int velYAsteroides; 
	private int cantAsteroides;

	private AudioManager audioManager;
	private int opcion = 1;
	private boolean keyDownPressed;
	private boolean keyUpPressed;
	private List<PowerUp> powerUps;

	private boolean paused;
	private BuqueGuerra nave;
	private  ArrayList<Ball2> balls1 = new ArrayList<>();
	private  ArrayList<Ball2> balls2 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();

	private ArrayList<PowerUpBall> powersOne = new ArrayList<>();
	private ArrayList<PowerUpBall> powersTwo = new ArrayList<>();
	private float powerUpTimer;
	private final float timeBetweenPowerUps; // Puedes ajustar el tiempo entre power-ups según tus necesidades
	private int powerUpCount;
	private final int maxPowerUps = 5;

	public PantallaJuego(SpaceNavigation game, int ronda, int vidas, int score,  
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {

		this.game = game;
		this.ronda = ronda;
		this.score = score;
		boolean paused = false;
		this.velXAsteroides = velXAsteroides;
		this.velYAsteroides = velYAsteroides;
		this.cantAsteroides = cantAsteroides;
		this.fondo = new Texture(Gdx.files.internal("sea-background.png"));

		this.timeBetweenPowerUps = 10f; // Puedes ajustar el tiempo entre power-ups según tus necesidades
		this.powerUpTimer = 0;
		this.powerUpCount = 0;
		audioManager = audioManager.getInstance();

		batch = game.getBatch();
		camera = new OrthographicCamera();	
		camera.setToOrtho(false, 800, 640);
		//inicializar assets; musica de fondo y efectos de sonido
		explosionSound = Gdx.audio.newSound(Gdx.files.internal("explosion.ogg"));
		pauseSound = Gdx.audio.newSound(Gdx.files.internal("pause.mp3"));

		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("piano-loops.wav")); //
		gameMusic.setLooping(true);
		audioManager.playMusic(gameMusic);

	    // cargar imagen de la nave, 64x64   
	    nave = new BuqueGuerra(Gdx.graphics.getWidth()/2-50,30,new Texture(Gdx.files.internal("MainShip3.png")),
	    				Gdx.audio.newSound(Gdx.files.internal("hurt.ogg")), 
	    				new Texture(Gdx.files.internal("Rocket2.png")), 
	    				Gdx.audio.newSound(Gdx.files.internal("pop-sound.mp3"))); 
        nave.setVidas(vidas);

        //crear asteroides
        Random r = new Random();
	    for (int i = 0; i < cantAsteroides; i++) {
	        Ball2 bb = new Ball2(r.nextInt((int)Gdx.graphics.getWidth()),
	  	            50+r.nextInt((int)Gdx.graphics.getHeight()-50),
	  	            20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4), 
	  	            new Texture(Gdx.files.internal("aGreyMedium4.png")));	   
	  	    balls1.add(bb);
	  	    balls2.add(bb);
	  	}
		//Crear Bolas (powerUp)
		Random s = new Random();
		for (int i = 0; i < powerUpCount; i++) {
			PowerUpBall cc = new PowerUpBall(r.nextInt((int)Gdx.graphics.getWidth()),
					50+r.nextInt((int)Gdx.graphics.getHeight()-50),
					20+r.nextInt(10), velXAsteroides+r.nextInt(4), velYAsteroides+r.nextInt(4),
					new Texture(Gdx.files.internal("bolaroja1.png")));
			powersOne.add(cc);
			powersTwo.add(cc);
		}


	}
    
	public void dibujaEncabezado() {
		CharSequence str = "Vidas: "+nave.getVidas()+" Ronda: "+ronda;
		game.getFont().getData().setScale(2f);		
		game.getFont().draw(batch, str, 10, 30);
		game.getFont().draw(batch, "Score:"+this.score, Gdx.graphics.getWidth()-150, 30);
		game.getFont().draw(batch, "HighScore:"+game.getHighScore(), Gdx.graphics.getWidth()/2-100, 30);
	}
	@Override
	public void render(float delta) {


		//lógica de la pausa del juego
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			if(paused)
			{
				audioManager.playMusic(gameMusic);
			}
			else{
				gameMusic.pause(); // Pausar la música al entrar en el estado de pausa
				audioManager.playSound(pauseSound);
			}
			paused = !paused;
		}

		if(!paused){

			powerUpTimer += delta;
		  Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		  batch.begin();
		  batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		  dibujaEncabezado();

			if (powerUpTimer > timeBetweenPowerUps) {
				generarNuevoPowerUp();
				powerUpTimer = 0; // Reiniciar el temporizador
				powerUpCount ++; //incrementar el contador
			}
		  if (!nave.estaHerido()) {
			  // colisiones entre balas y asteroides y su destruccion
			  for (int i = 0; i < balas.size(); i++) {
					Bullet b = balas.get(i);
					b.update();
					for (int j = 0; j < balls1.size(); j++) {
					  if (b.checkCollision(balls1.get(j))) {
						 audioManager.playSound(explosionSound);
						 balls1.remove(j);
						 balls2.remove(j);
						 j--;
						 score +=10;
					  }
					}

				 //   b.draw(batch);
					if (b.isDestroyed()) {
						balas.remove(b);
						i--; //para no saltarse 1 tras eliminar del arraylist
					}
			  }

			  for (int i = 0; i < powersOne.size(); i++) {
				  PowerUpBall powerOne = powersOne.get(i);
				  for (int j = 0; j < powersTwo.size(); j++) {
					  PowerUpBall powerTwo = powersTwo.get(j);
					  if (i < j) {
						  powerOne.checkCollision(powerTwo);
					  }
				  }
				  // Comprobar la colisión con la nave y eliminar el power-up si es necesario
				  if (nave.checkCollisionPowerUp(powerOne)) {
					  // Elimina el power-up
					  powersOne.remove(i);
					  powersTwo.remove(powerOne);
					  i--;
				  }
			  }

			  //actualizar movimiento de asteroides dentro del area
			  for (Ball2 ball : balls1) {
				  ball.update();
			  }

			  for (PowerUpBall powerUpBall : powersOne) {
				  powerUpBall.draw(batch);
			  }
				// Actualizar movimiento de powerUps dentro del área

			  for (PowerUpBall powerUpBall : powersOne) {
				  powerUpBall.update();
			  }

			  //colisiones entre asteroides y sus rebotes
			  for (int i=0;i<balls1.size();i++) {
				Ball2 ball1 = balls1.get(i);
				for (int j=0;j<balls2.size();j++) {
				  Ball2 ball2 = balls2.get(j);
				  if (i<j) {
					  ball1.checkCollision(ball2);

				  }
				}
			  }//colisiones entre powerUps y sus rebotes
			  for (int i=0;i<powersOne.size();i++) {
				  PowerUpBall powerOne = powersTwo.get(i);
				  for (int j=0;j<powersTwo.size();j++) {
					  PowerUpBall powerTwo = powersTwo.get(j);
					  if (i<j) {
						  powerOne.checkCollision(powerTwo);

					  }
				  }
			  }
		  }
		  //dibujar balas
		 for (Bullet b : balas) {
			  b.draw(batch);
		  }
		  nave.draw(batch, this);
		  //dibujar asteroides y manejar colision con nave
		  for (int i = 0; i < balls1.size(); i++) {
				Ball2 b=balls1.get(i);
				b.draw(batch);
				  //perdió vida o game over
				  if (nave.checkCollision(b)) {
					//asteroide se destruye con el choque

					 balls1.remove(i);
					 balls2.remove(i);
					 i--;
			  }
			}

		  if (nave.estaDestruido()) {
			if (score > game.getHighScore())
				game.setHighScore(score);
			Screen ss = new PantallaGameOver(game);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		  }
		  batch.end();
		  //nivel completado
		  if (balls1.size()==0) {
			Screen ss = new PantallaJuego(game,ronda+1, nave.getVidas(), score,
					velXAsteroides+3, velYAsteroides+3, cantAsteroides+10);
			ss.resize(1200, 800);
			game.setScreen(ss);
			dispose();
		  }
		}else {
			// Dibujar una pantalla de pausa
			pause();
			showPauseScreen();
		}



	}
	private void generarNuevoPowerUp() {
		Random random = new Random();
		PowerUpBall nuevoPowerUp = new PowerUpBall(
				random.nextInt((int) Gdx.graphics.getWidth()),
				50 + random.nextInt((int) Gdx.graphics.getHeight() - 50),
				20 + random.nextInt(10),
				velXAsteroides + random.nextInt(4),
				velYAsteroides + random.nextInt(4),
				new Texture(Gdx.files.internal("bolaroja1.png"))
		);

		powersOne.add(nuevoPowerUp);
		powersTwo.add(nuevoPowerUp);
	}

	private void imprimirFlecha()
	{
		switch(opcion)
		{
			case 1:
				game.getFont().draw(batch, "->", Gdx.graphics.getWidth() / 2.f - 400, Gdx.graphics.getHeight() / 2.f + 200);
				break;
			case 2:
				game.getFont().draw(batch, "->", Gdx.graphics.getWidth() / 2.f - 400, Gdx.graphics.getHeight() / 2.f + 100);

				break;
			case 3:
				game.getFont().draw(batch, "->", Gdx.graphics.getWidth() / 2.f - 400, Gdx.graphics.getHeight() / 2.f);
				break;
		}

	}



	private void cambiarAjustes()
	{
		switch (opcion)
		{
			case 1: //volumen global
				//aumentar volumen
				if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
				{
					if(audioManager.getMasterVolume() < 1.0f)
					{
						audioManager.setMasterVolume(Math.round((audioManager.getMasterVolume() + 0.10f) * 10.0f) / 10.0f);
					}
				}
				//reducir volumen
				if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
				{
					if(audioManager.getMasterVolume() > 0)
					{
						audioManager.setMasterVolume(Math.round((audioManager.getMasterVolume() - 0.10f) * 10.0f) / 10.0f);
					}
				}

				break;
			case 2: //volumen de SFX
				//aumentar volumen
				if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
				{
					if(audioManager.getSoundVolume() < 1.0f)
					{
						audioManager.setSoundVolume(Math.round((audioManager.getSoundVolume() + 0.10f) * 10.0f) / 10.0f);
					}

				}
				// reducir volumen
				if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
				{
					if(audioManager.getSoundVolume() > 0)
					{
						audioManager.setSoundVolume(Math.round((audioManager.getSoundVolume() - 0.10f) * 10.0f) / 10.0f);
					}
				}
				break;
			case 3: //volumen de música
				if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT))
				{
					if(audioManager.getMusicVolume() < 1.f)
					{
						audioManager.setMusicVolume(Math.round((audioManager.getMusicVolume() + 0.10f) * 10.0f) / 10.0f);
					}
					break;
				}
				if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT))
				{
					if(audioManager.getMusicVolume() > 0)
					{
						audioManager.setMusicVolume(Math.round((audioManager.getMusicVolume() - 0.10f) * 10.0f) / 10.0f);
					}
				}
			break;
		}
	}

	//opcion 1: masterVolume ; 2: SoundVolume ; 3:
	private void showPauseScreen() {
		// Configurar la transparencia para hacer la pantalla de pausa
		Gdx.gl.glClearColor(0, 0, 0, 0.5f); // Fondo transparente
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		// Dibujar el texto "PAUSE" en el centro de la pantalla
		batch.begin();

		//lógica del movimiento de las opciones de menú
		if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)){
			opcion++;
			if(opcion > 3) opcion = 1;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			opcion--;
			if(opcion < 1) opcion = 3;
		}

		imprimirFlecha();
		cambiarAjustes();
		game.getFont().getData().setScale(3f);
		game.getFont().draw(batch, "PAUSE", Gdx.graphics.getWidth() / 2.f - 70, Gdx.graphics.getHeight() / 2.f + 300);
		game.getFont().draw(batch, "Volumen general: " + audioManager.getMasterVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f + 200);
		game.getFont().draw(batch, "Volumen SFX: " + audioManager.getSoundVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f + 100);
		game.getFont().draw(batch, "Volumen música: " + audioManager.getMusicVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f);


		batch.end();
	}
    public boolean agregarBala(Bullet bb) {
    	return balas.add(bb);
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		audioManager.playMusic(gameMusic);
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		if (!paused) {
			paused = true;
			gameMusic.pause(); // Pausar la música al entrar en el estado de pausa
			audioManager.playSound(pauseSound);
		}
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		if (paused) {
			paused = false;
			audioManager.playMusic(gameMusic); // Reanudar la música al salir del estado de pausa
		}
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		this.explosionSound.dispose();
		this.gameMusic.dispose();
		this.pauseSound.dispose();
	}
   
}
