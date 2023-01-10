package ensa.project_vt;

import java.util.Objects;

public class Caption {
    private int id;
    private double start;
    private double end;
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getEnd() {
        return end;
    }

    public void setEnd(double end) {
        this.end = end;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Caption{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caption caption = (Caption) o;
        return id == caption.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Caption(int id) {
        this.id = id;
    }
}
