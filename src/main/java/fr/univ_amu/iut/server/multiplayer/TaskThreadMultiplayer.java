package fr.univ_amu.iut.server.multiplayer;

import fr.univ_amu.iut.database.dao.DAOQuizJDBC;
import fr.univ_amu.iut.database.table.Qcm;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class TaskThreadMultiplayer implements Runnable{
    private Socket sockClient;
    private BufferedReader in;
    private BufferedWriter out;
    private String str;
    private List<Qcm> quiz;

    public TaskThreadMultiplayer(Socket sockClient, List<Qcm> quiz) throws IOException {
        this.sockClient = sockClient;
        this.in = new BufferedReader(new InputStreamReader(sockClient.getInputStream()));
        this.out = new BufferedWriter(new OutputStreamWriter(sockClient.getOutputStream()));
        this.quiz = quiz;
    }

    public void giveQuestions() throws IOException {
        for(Qcm q : quiz) {
            out.write(q.getQuestion());
            out.newLine();
            out.write(q.getAnswer1());
            out.newLine();
            out.write(q.getAnswer2());
            out.newLine();
            out.write(q.getAnswer3());
            out.newLine();

            out.flush();

            if ((str = in.readLine()) != null) {
                if (q.getTrueAnswer() == Integer.parseInt(str)) {
                    out.write("CORRECT_ANSWER_FLAG");
                } else {
                    out.write("WRONG_ANSWER_FLAG");
                }
                out.newLine();
                out.flush();
            } else {
                stopRunning();
            }
        }
    }

    public void stopRunning() throws IOException {
        sockClient.close();
    }

    @Override
    public void run() {
        try {
            giveQuestions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}