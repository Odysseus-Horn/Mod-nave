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
import com.mygdx.game.LevelPlanner.BombaMarina;
import com.mygdx.game.LevelPlanner.Level1Factory;
import com.mygdx.game.LevelPlanner.Level2Factory;
import com.mygdx.game.LevelPlanner.LevelFactory;

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

	private boolean paused;
	private Nave nave;
	private  ArrayList<BombaMarina> balls1 = new ArrayList<>();
	private  ArrayList<BombaMarina> balls2 = new ArrayList<>();
	private  ArrayList<Bullet> balas = new ArrayList<>();

	private ArrayList<PowerUpBall> powersOne = new ArrayList<>();
	private ArrayList<PowerUpBall> powersTwo = new ArrayList<>();
	private ArrayList<PowerUpBall> powersThree = new ArrayList<>();
	private ArrayList<PowerUpBall> powersFour = new ArrayList<>();
	private ArrayList<PowerUpBall> powersFive = new ArrayList<>();
	private ArrayList<PowerUpBall> powersSix = new ArrayList<>();
	
	private float powerUpTimer;
	private final float timeBetweenPowerUps; // Puedes ajustar el tiempo entre power-ups según tus necesidades
	private int powerUpCount;
	private final int maxPowerUps = 5;
	private float elapsedTimeSincePowerUp;
	private String powerUp;


	public PantallaJuego(SpaceNavigation game,Nave nave ,int ronda, int vidas, int score,
			int velXAsteroides, int velYAsteroides, int cantAsteroides) {

		//inicializar datos del objeto pantall
		this.game = game;
		this.nave = nave;
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
		this.elapsedTimeSincePowerUp = 0;
		
		
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

		LevelFactory fabricaNivel1 = new Level1Factory();
		LevelFactory fabricaNivel2 = new Level2Factory();





		//crear asteroides
		if(ronda == 1){
			balls1 = fabricaNivel1.crearObstaculos(cantAsteroides, velXAsteroides,velYAsteroides);
			balls2.addAll(balls1);
		}
		else{
			balls1 = fabricaNivel2.crearObstaculos(cantAsteroides, velXAsteroides,velYAsteroides);
			balls2.addAll(balls1);
		}



		//Random r = new Random();


		//Crear Bolas (powerUp-vidas)
		Random s = new Random();
		for (int i = 0; i < powerUpCount; i++) {
			PowerUpBall cc = new PowerUpBall(s.nextInt((int)Gdx.graphics.getWidth()),
					50+s.nextInt((int)Gdx.graphics.getHeight()-50),
					20+s.nextInt(10), velXAsteroides+s.nextInt(4), velYAsteroides+s.nextInt(4),
					new Texture(Gdx.files.internal("bolaroja1.png")));
			powersOne.add(cc);
			powersTwo.add(cc);
		}
		//Crear Bolas (powerUp vel)
				Random t = new Random();
				for (int i = 0; i < powerUpCount; i++) {
					PowerUpBall dd = new PowerUpBall(t.nextInt((int)Gdx.graphics.getWidth()),
							50+t.nextInt((int)Gdx.graphics.getHeight()-50),
							20+t.nextInt(10), velXAsteroides+t.nextInt(4), velYAsteroides+t.nextInt(4),
							new Texture(Gdx.files.internal("bolazul1.png")));
					powersThree.add(dd);
					powersFour.add(dd);
				}
				Random w = new Random();
				for (int i = 0; i < powerUpCount; i++) {
					PowerUpBall ee = new PowerUpBall(w.nextInt((int)Gdx.graphics.getWidth()),
							50+w.nextInt((int)Gdx.graphics.getHeight()-50),
							20+w.nextInt(10), velXAsteroides+w.nextInt(4), velYAsteroides+w.nextInt(4),
							new Texture(Gdx.files.internal("bolanegra1.png")));
					powersThree.add(ee);
					powersFour.add(ee);
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
				powerUp = generarNuevoPowerUpAleatorio();
				powerUpTimer = 0; // Reiniciar el temporizador
				powerUpCount ++; //incrementar el contador
			}
			/*
			if (elapsedTimeSincePowerUp >= 10) {
			    generarNuevoPowerUpAleatorio();
			    elapsedTimeSincePowerUp = 0;
			}*/

			//revisa si la nave no está herida
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
			//bola negra
			  for (int i = 0; i < powersFive.size(); i++) {
				  PowerUpBall powerFive = powersFive.get(i);
				  for (int j = 0; j < powersSix.size(); j++) {
					  PowerUpBall powerSix = powersSix.get(j);
					  if (i < j) {
						  powerFive.checkCollision(powerSix);
					  }
				  }
				  // Comprobar la colisión con la nave y eliminar el power-up si es necesario
				  if (nave.checkCollisionPowerUp(powerFive,powerUp)) {
					  // Elimina el power-up
					  powersFive.remove(i);
					  powersSix.remove(powerFive);
					  i--;
					  score+= nave.getScore();
				  }
			  }
			  //bola azul
			  for (int i = 0; i < powersThree.size(); i++) {
				  PowerUpBall powerThree = powersThree.get(i);
				  for (int j = 0; j < powersFour.size(); j++) {
					  PowerUpBall powerFour = powersFour.get(j);
					  if (i < j) {
						  powerThree.checkCollision(powerFour);
					  }
				  }
				  // Comprobar la colisión con la nave y eliminar el power-up si es necesario
				  if (nave.checkCollisionPowerUp(powerThree,powerUp)) {
					  // Elimina el power-up
					  powersThree.remove(i);
					  powersFour.remove(powerThree);
					  i--;
				  }
			  }
			  //bola roja
			  for (int i = 0; i < powersOne.size(); i++) {
				  PowerUpBall powerOne = powersOne.get(i);
				  for (int j = 0; j < powersTwo.size(); j++) {
					  PowerUpBall powerTwo = powersTwo.get(j);
					  if (i < j) {
						  powerOne.checkCollision(powerTwo);
					  }
				  }
				  // Comprobar la colisión con la nave y eliminar el power-up si es necesario
				  if (nave.checkCollisionPowerUp(powerOne,powerUp)) {
					  // Elimina el power-up
					  powersOne.remove(i);
					  powersTwo.remove(powerOne);
					  i--;
				  }
			  }

			  //actualizar movimiento de asteroides dentro del area
			  for (BombaMarina ball : balls1) {
				  ball.update();
			  }

			  for (PowerUpBall powerUpBall : powersOne) {
				  powerUpBall.draw(batch);
			  }
				// Actualizar movimiento de powerUps dentro del área bola roja

			  for (PowerUpBall powerUpBall : powersOne) {
				  powerUpBall.update();
			  }
			  for (PowerUpBall powerUpBall : powersThree) {
				  powerUpBall.draw(batch);
			  }
				// Actualizar movimiento de powerUps dentro del área bola azul

			  for (PowerUpBall powerUpBall : powersThree) {
				  powerUpBall.update();
			  }
			// Actualizar movimiento de powerUps dentro del área bola negra

			  for (PowerUpBall powerUpBall : powersFive) {
				  powerUpBall.update();
			  }
			  for (PowerUpBall powerUpBall : powersFive) {
				  powerUpBall.draw(batch);
			  }
			  //colisiones entre asteroides y sus rebotes
			  for (int i=0;i<balls1.size();i++) {
				BombaMarina ball1 = balls1.get(i);
				for (int j=0;j<balls2.size();j++) {
				  BombaMarina ball2 = balls2.get(j);
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
			  for (int i=0;i<powersThree.size();i++) {
				  PowerUpBall powerOne = powersFour.get(i);
				  for (int j=0;j<powersFour.size();j++) {
					  PowerUpBall powerTwo = powersFour.get(j);
					  if (i<j) {
						  powerOne.checkCollision(powerTwo);

					  }
				  }
			  }
			  for (int i=0;i<powersFive.size();i++) {
				  PowerUpBall powerOne = powersFive.get(i);
				  for (int j=0;j<powersSix.size();j++) {
					  PowerUpBall powerTwo = powersSix.get(j);
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
				BombaMarina b=balls1.get(i);
				b.draw(batch);
				  //perdió vida o game over
				  if (nave.checkCollision(b)) {
					//asteroide se destruye con el choque

					 balls1.remove(i);
					 balls2.remove(i);
					 i--;
			  }
			}

		  //verifica si el jugador perdió
		  if (nave.getVidas() == 0) {
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
			Screen ss = new PantallaJuego(game,nave, ronda+1, nave.getVidas(), score,
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
	/*
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
	*/
	private String generarNuevoPowerUpAleatorio() {
	    Random random = new Random();
	    int tipoPowerUp = random.nextInt(3) + 1; // Números entre 1 y 4
	    //tipoPowerUp = 3;
	    switch (tipoPowerUp) {
	        case 1:
	            powersOne.add(crearPowerUp("bolaroja1.png"));
	            powersTwo.add(crearPowerUp("bolaroja1.png"));
	            return "bolaroja1";
	            		
	            //break;
	        case 2:
	            powersThree.add(crearPowerUp("bolazul1.png"));
	            powersFour.add(crearPowerUp("bolazul1.png"));
	            return "bolazul";
	            //break;
	        case 3:
	            powersFive.add(crearPowerUp("bolanegra1.png"));
	            powersSix.add(crearPowerUp("bolanegra1.png"));
	            return"bolanegra1";
	        case 4:
	            powersFour.add(crearPowerUp("bolamarilla.png"));
	            powersOne.add(crearPowerUp("bolamarilla.png"));
	            return"bolaamarilla";
	    }
		return powerUp;
	}
	private PowerUpBall crearPowerUp(String textura) {
	    Random random = new Random();
	    return new PowerUpBall(
	            random.nextInt((int) Gdx.graphics.getWidth()),
	            50 + random.nextInt((int) Gdx.graphics.getHeight() - 50),
	            20 + random.nextInt(10),
	            velXAsteroides + random.nextInt(4),
	            velYAsteroides + random.nextInt(4),
	            new Texture(Gdx.files.internal(textura))
	    );
	}

	//imprime la flecha según la opción del código
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
			case 4:
				game.getFont().draw(batch, "->", Gdx.graphics.getWidth() / 2.f - 400, Gdx.graphics.getHeight() / 2.f - 100);
				break;
		}

	}


	//funcion que cambia el volumen de cada opcion en el menu de pausa
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
			case 4:
				if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
					game.setScreen(new PantallaSeleccionNave(game));
					dispose();
				}

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
			if(opcion > 4) opcion = 1;
		}
		if(Gdx.input.isKeyJustPressed(Input.Keys.UP))
		{
			opcion--;
			if(opcion < 1) opcion = 4;
		}

		imprimirFlecha();
		cambiarAjustes();
		game.getFont().getData().setScale(3f);
		game.getFont().draw(batch, "PAUSE", Gdx.graphics.getWidth() / 2.f - 70, Gdx.graphics.getHeight() / 2.f + 300);
		game.getFont().draw(batch, "Volumen general: " + audioManager.getMasterVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f + 200);
		game.getFont().draw(batch, "Volumen SFX: " + audioManager.getSoundVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f + 100);
		game.getFont().draw(batch, "Volumen música: " + audioManager.getMusicVolume(), Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f);
		game.getFont().draw(batch, "Salir del juego", Gdx.graphics.getWidth() / 2.f - 200, Gdx.graphics.getHeight() / 2.f - 100);

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
