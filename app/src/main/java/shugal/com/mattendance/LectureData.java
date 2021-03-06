package shugal.com.mattendance;

/**
 * Created by abhishek on 21/10/15.
 */
public class LectureData {
    private int id;
    private String lecture_name;
    private float presents;
    private float absents;
    private float percent;


    public LectureData() {

    }


    public LectureData(String name, float presents, float absents) {
        this.lecture_name = name;
        this.presents = presents;
        this.absents = absents;

        float total = this.presents + this.absents;

        if (total == 0) {
            this.percent = 0;
        } else {
            this.percent = (this.presents/total)*100;
        }
    }

    public String get_lecture_name() {
        return this.lecture_name;
    }

    public float get_presents() {
        return this.presents;
    }

    public float get_absents() {
        return this.absents;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public int get_id() { return this.id; }


    public void set_lecture_name(String name) { this.lecture_name = name; }

    public void set_absent(float _absent) {
        this.absents = _absent;
    }

    public void set_present(float _present) {
        this.presents = _present;
    }

    public float getPercent() {
        float total = this.presents + this.absents;

        if (total == 0) {
            this.percent = 0;
        } else {
            this.percent = (this.presents/total)*100;
        }

        return this.percent;
    }

    public void setPercent() {

        float total = this.presents + this.absents;

        if (total == 0) {
            this.percent = 0;
        } else {
            this.percent = (this.presents/total)*100;
        }
    }
}
