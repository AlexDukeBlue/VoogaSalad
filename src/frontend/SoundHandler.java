package frontend;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


//Use MediaPlayer, all of Applet is deprecated
public class SoundHandler {

	public void playSound(Boolean loop, String urlString) {
		if (urlString.substring(urlString.length()-4).equals(".wav") || urlString.substring(urlString.length()-4).equals(".mp3")) {
			URL url = getClass().getClassLoader().getResource(urlString);
			Media sound = new Media(url.toString());
			MediaPlayer clip = new MediaPlayer(sound);
			clip.play();
			if (loop) {
				clip.setCycleCount(MediaPlayer.INDEFINITE);
			}
		}
		else {
			System.out.println("Audio file must be in .wav or .mp3 format");
		}
	}
}
