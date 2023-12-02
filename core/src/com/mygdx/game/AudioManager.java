package com.mygdx.game;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input;


/* CLASE IMPLEMENTADA CON SINGLETON
* esta clase es utilizada para poder ajustar, reproducir y normalizar
*  todos los sonidos a un mismo nivel a través de variables de la misma clase.
*
*
*
*
* */
public class AudioManager {
    private static AudioManager instance;

    //volumenes generales
    private float masterVolume;
    private float musicVolume;
    private float soundVolume;

    //constructor
    private AudioManager()
    {
        masterVolume = 1f;
        musicVolume = 1f;
        soundVolume = 1f;
    }

    //getter de la instancia
    public static AudioManager getInstance()
    {
        if(instance == null)
        {
            instance = new AudioManager();
        }
        return instance;
    }

    //se utiliza para reproducir un sonido cualquiera
    public void playSound(Sound sonidoAReproducir)
    {
        sonidoAReproducir.play(masterVolume * soundVolume);
    }

    //se utiliza para reproducir cualquier tipo de música
    public void playMusic(Music musicaAReproducir)
    {
        musicaAReproducir.setVolume(masterVolume * musicVolume);
        musicaAReproducir.play();
    }

    // Getters y Setters para los atributos de volumen
    public float getMasterVolume() {
        return masterVolume;
    }

    public void setMasterVolume(float masterVolume) {
        this.masterVolume = masterVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = musicVolume;
    }

    public float getSoundVolume() {
        return soundVolume;
    }

    public void setSoundVolume(float soundVolume) {
        this.soundVolume = soundVolume;
    }

}
