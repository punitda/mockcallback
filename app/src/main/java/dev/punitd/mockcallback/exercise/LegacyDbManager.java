package dev.punitd.mockcallback.exercise;

public class LegacyDbManager {

    public void syncExercise(ExerciseSessionRecord record, Runnable completionHandler) {
        // Saves something in DB using Executor Service and notifies completion via runnable
        completionHandler.run();
    }
}
