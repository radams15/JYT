package uk.co.therhys.YT;

public interface VidListener {
    void getVideo(Video video);

    void vidFetchCompleted();

    void fetchProgress(float proportion);
}
