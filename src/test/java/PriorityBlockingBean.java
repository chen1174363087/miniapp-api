public class PriorityBlockingBean implements Comparable<PriorityBlockingBean> {

    Integer value;
    String name;

    public PriorityBlockingBean(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(PriorityBlockingBean o) {
        return (this.value  > o.getValue() ? -1 : (this.value  == o.getValue() ? 0 : 1));
    }

    @Override
    public String toString() {
        return "PriorityBlockingBean{" +
                "value=" + value +
                ", name='" + name + '\'' +
                '}';
    }
}
