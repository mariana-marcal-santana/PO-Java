package xxl.DesignPatterns.Observer;

/**
 * Interface that represents a subject.
 */
public interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
}
