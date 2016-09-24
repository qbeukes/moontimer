package za.co.mosthighmountain.simpletimer;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Quintin on 8/23/2015.
 */
public class BellPlayer {
    private MediaPlayer[] mediaPlayers;

    public BellPlayer(Context context) {
        mediaPlayers = new MediaPlayer[] {
                MediaPlayer.create(context, R.raw.bell1),
                MediaPlayer.create(context, R.raw.bell2),
                MediaPlayer.create(context, R.raw.bell3)
        };
    }

    public void playBell(int num) {
        if (num < 1 || num > 3) {
            throw new RuntimeException("Invalid bell number " + num);
        }

        for (MediaPlayer player : mediaPlayers) {
            if (player.isPlaying()) {
                return;
            }
        }

        System.out.println(mediaPlayers[0].isPlaying());

        mediaPlayers[num - 1].start();
    }
}
