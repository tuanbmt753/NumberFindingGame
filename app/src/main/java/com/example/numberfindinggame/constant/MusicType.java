package com.example.numberfindinggame.constant;

import com.example.numberfindinggame.R;
import com.example.numberfindinggame.model.ManChoi;
import com.example.numberfindinggame.model.NhacNen;

import java.util.ArrayList;
import java.util.List;

public class MusicType {
    private ArrayList<NhacNen> dsNhacNen = new ArrayList<>();;

    public MusicType() {
        dsNhacNen.clear();
        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen,
                "Eternum - Silverware (feat. Mia Pfirrman) by Katnip",
                "Eternum - Silverware (feat. Mia Pfirrman) by Katnip",
                "https://drive.google.com/file/d/1n-FUSuE9ImS7XtTU9Iqw01puZiawjUT7/view?usp=drive_link",
                "https://www.youtube.com/watch?v=0qncsk2Hh1A&list=PLgbMs1JkhKJN4g8M4TpUFuzhG3rTbnSXd&index=2"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen2,
                "Eternum - Besame by Lu-Ni",
                "Eternum - Besame by Lu-Ni",
                "https://drive.google.com/file/d/1ffon1_a4ljKOriZof-LMPylIAZRzmpYm/view?usp=drive_link",
                "https://www.youtube.com/watch?v=UDUD0lf_GRw&list=PLgbMs1JkhKJN4g8M4TpUFuzhG3rTbnSXd&index=3"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen3,
                "Eternum - City by Allie Cabal & Two Feet",
                "Eternum - City by Allie Cabal & Two Feet",
                "https://drive.google.com/file/d/1XCACmK0C1SpRHSY7aNsJnx0RRjTlCuf_/view?usp=drive_link",
                "https://www.youtube.com/watch?v=UgQ_4AROCzU&list=PLgbMs1JkhKJN4g8M4TpUFuzhG3rTbnSXd&index=4"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen4,
                "Eternum - blah blah blah by Camille de la Cruz & WEARETHEGOOD",
                "Eternum - blah blah blah by Camille de la Cruz & WEARETHEGOOD",
                "https://drive.google.com/file/d/1KtD5rD8xjPHvauytlduZOKSZouVJDP6g/view?usp=drive_link",
                "https://www.youtube.com/watch?v=wFvVgKc6d6Y&list=PLgbMs1JkhKJN4g8M4TpUFuzhG3rTbnSXd&index=5"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen5,
                "OST Eternum - Good Friends",
                "OST Eternum - Good Friends",
                "https://drive.google.com/file/d/18mCxt_wCioJI2rd91UGug63z1pzOgkxw/view?usp=drive_link",
                "https://www.youtube.com/watch?v=KLM2RACFGJc&list=PLgbMs1JkhKJN4g8M4TpUFuzhG3rTbnSXd&index=6"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen6,
                "Irokz - Goodbye My Love | Phouse | NCS - Copyright Free Music",
                "Irokz - Goodbye My Love | Phouse | NCS - Copyright Free Music",
                "https://drive.google.com/file/d/1JK_Pe0loOySuM6kzRXbD68nTluAVzgCu/view?usp=sharing",
                "https://www.youtube.com/watch?v=PCAptQGb6K4"
        ));

        dsNhacNen.add(new NhacNen(
                R.raw.nhac_nen7,
                "「Nightcore」 VOID - Jim Yosef ♡ (Lyrics)",
                "「Nightcore」 VOID - Jim Yosef ♡ (Lyrics)",
                "https://drive.google.com/file/d/1YS5QZMaE2vVlI71ybaurw7CWdHartbKZ/view?usp=drive_link",
                "https://www.youtube.com/watch?v=VNTi1sfQXvM"
        ));
    }

    public ArrayList<NhacNen> getDsNhacNen() {
        return dsNhacNen;
    }

    public void setDsNhacNen(ArrayList<NhacNen> dsNhacNen) {
        this.dsNhacNen = dsNhacNen;
    }
}
