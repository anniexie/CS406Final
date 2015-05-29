//Authors: Annie Xie and Justin Zhang
//CS406 Advanced Programming Final Project

public class User implements Comparable<User> {
  private int score;
  private String name;
  
  public User(int _score, String _name) {
    score = _score;
    name = _name;
  }
  public int getScore() {
    return score;
  }
  public String getName() {
    return name;
  }
  @Override
  public int compareTo(User that) {
    if(score != (that.score))
      return that.score - score;
    return name.compareTo(that.name);
  }
}