

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;

public class SortingVisualizer extends JFrame {
    private static final int ARRAY_SIZE = 50;
    private static final int ARRAY_MIN_VALUE = 10;
    private static final int ARRAY_MAX_VALUE = 500;
    private static final int ARRAY_BAR_WIDTH = 10;
    private static final int DELAY_MS = 50;

    private volatile boolean stopFlag = false;

    private int[] array;
    private ArrayPanel arrayPanel;
    private JButton startButton;
    private JButton stopButton;
    private JButton restartButton; // New button for restart
    private JComboBox<String> algorithmComboBox;

    public SortingVisualizer() {
        setTitle("Sorting Algorithm Visualizer");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
        arrayPanel = new ArrayPanel(array);

        JPanel controlPanel = new JPanel();
        startButton = new JButton("Start");
        stopButton = new JButton("Stop");
        restartButton = new JButton("Restart"); // Initialize restart button
        stopButton.setEnabled(false);
        algorithmComboBox = new JComboBox<>(
                new String[]{"Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"});

        startButton.addActionListener(e -> {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            restartButton.setEnabled(false); // Disable restart button when sorting is ongoing
            startSorting();
        });

        stopButton.addActionListener(e -> stopSorting());

        restartButton.addActionListener(e -> {
            array = generateRandomArray(ARRAY_SIZE, ARRAY_MIN_VALUE, ARRAY_MAX_VALUE);
            arrayPanel.setArray(array);
            arrayPanel.repaint();
            startButton.setEnabled(true);
            restartButton.setEnabled(true); // Enable restart button after restarting
        });

        controlPanel.add(startButton);
        controlPanel.add(stopButton);
        controlPanel.add(restartButton); // Add restart button to control panel
        controlPanel.add(new JLabel("Algorithm:"));
        controlPanel.add(algorithmComboBox);

        add(arrayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private int[] generateRandomArray(int size, int minValue, int maxValue) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
        }
        return array;
    }

    private void startSorting() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();
        switch (selectedAlgorithm) {
            case "Bubble Sort":
                new BubbleSortThread().start();
                break;
            case "Selection Sort":
                new SelectionSortThread().start();
                break;
            case "Insertion Sort":
                new InsertionSortThread().start();
                break;
            case "Merge Sort":
                new MergeSortThread().start();
                break;
            case "Quick Sort":
                new QuickSortThread().start();
                break;
        }
    }

    private void stopSorting() {
        stopFlag = true;
    }

    private class BubbleSortThread extends Thread {
        public void run() {
            for (int i = 0; i < array.length - 1; i++) {
                for (int j = 0; j < array.length - i - 1; j++) {
                    if (array[j] > array[j + 1]) {
                        int temp = array[j];
                        array[j] = array[j + 1];
                        array[j + 1] = temp;
                        arrayPanel.repaint();
                        try {
                            Thread.sleep(DELAY_MS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (stopFlag)
                        return;
                }
                if (stopFlag)
                    return;
            }
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            restartButton.setEnabled(true); // Enable restart button after sorting is finished
        }
    }

    private class SelectionSortThread extends Thread {
        public void run() {
            for (int i = 0; i < array.length - 1; i++) {
                int minIndex = i;
                for (int j = i + 1; j < array.length; j++) {
                    if (array[j] < array[minIndex]) {
                        minIndex = j;
                    }
                    if (stopFlag)
                        return;
                }
                int temp = array[minIndex];
                array[minIndex] = array[i];
                array[i] = temp;
                arrayPanel.repaint();
                try {
                    Thread.sleep(DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopFlag)
                    return;
            }
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            restartButton.setEnabled(true); // Enable restart button after sorting is finished
        }
    }

    private class InsertionSortThread extends Thread {
        public void run() {
            for (int i = 1; i < array.length; i++) {
                int key = array[i];
                int j = i - 1;
                while (j >= 0 && array[j] > key) {
                    array[j + 1] = array[j];
                    j = j - 1;
                    if (stopFlag)
                        return;
                }
                array[j + 1] = key;
                arrayPanel.repaint();
                try {
                    Thread.sleep(DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopFlag)
                    return;
            }
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            restartButton.setEnabled(true); // Enable restart button after sorting is finished
        }
    }

    private class MergeSortThread extends Thread {
        public void run() {
            mergeSort(array, 0, array.length - 1);
            arrayPanel.repaint();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            restartButton.setEnabled(true); // Enable restart button after sorting is finished
        }

        private void mergeSort(int arr[], int l, int r) {
            if (l < r) {
                int m = (l + r) / 2;
                mergeSort(arr, l, m);
                mergeSort(arr, m + 1, r);
                if (stopFlag)
                    return;
                merge(arr, l, m, r);
            }
        }

        private void merge(int arr[], int l, int m, int r) {
            int n1 = m - l + 1;
            int n2 = r - m;
            int L[] = new int[n1];
            int R[] = new int[n2];
            for (int i = 0; i < n1; ++i)
                L[i] = arr[l + i];
            for (int j = 0; j < n2; ++j)
                R[j] = arr[m + 1 + j];
            int i = 0, j = 0;
            int k = l;
            while (i < n1 && j < n2) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
                k++;
                arrayPanel.repaint();
                try {
                    Thread.sleep(DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopFlag)
                    return;
            }
            while (i < n1) {
                arr[k] = L[i];
                i++;
                k++;
            }
            while (j < n2) {
                arr[k] = R[j];
                j++;
                k++;
            }
        }
    }

    private class QuickSortThread extends Thread {
        public void run() {
            quickSort(array, 0, array.length - 1);
            arrayPanel.repaint();
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            restartButton.setEnabled(true); // Enable restart button after sorting is finished
        }

        private void quickSort(int arr[], int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }

        private int partition(int arr[], int low, int high) {
            int pivot = arr[high];
            int i = (low - 1);
            for (int j = low; j < high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
                arrayPanel.repaint();
                try {
                    Thread.sleep(DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (stopFlag)
                    return -1;
            }
            int temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            return i + 1;
        }
    }

    private class ArrayPanel extends JPanel {
        private int[] array;

        public ArrayPanel(int[] array) {
            this.array = array;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < array.length; i++) {
                int barHeight = array[i];
                int x = i * ARRAY_BAR_WIDTH;
                int y = getHeight() - barHeight;
                g.fillRect(x, y, ARRAY_BAR_WIDTH, barHeight);
            }
        }

        public void setArray(int[] array) {
            this.array = array;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SortingVisualizer visualizer = new SortingVisualizer();
            visualizer.setVisible(true);
        });
    }
}
