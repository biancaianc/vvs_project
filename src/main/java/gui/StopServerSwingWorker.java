package gui;

import javax.swing.*;

import static gui.WebServerControl.semaphore;

public class StopServerSwingWorker extends SwingWorker<Void, Void> {
    @Override
    protected Void doInBackground() throws Exception {
        semaphore.acquire();
        return null;
    }
}
