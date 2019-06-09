import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Picture;

import java.util.ArrayList;


public class SeamCarver {
    private final int BOUNDARY_ENERGY = 1000;
    Picture image;
    double[][] energyArray;

    public SeamCarver(Picture picture) {
        image = picture;
        calculateEnergyArray();
    }

    public Picture picture() {
        return image;
    }

    public int width() {
        return image.width();
    }

    public int height() {
        return image.height();
    }

    public  double energy(int x, int y) {
        return energyArray[x][y];
    }


    public int[] findHorizontalSeam() {
        transposeEnergyMap();
        int[] seam = findVerticalSeam();
        transposeEnergyMap();
        return seam;
    }

    public int[] findVerticalSeam() {

        double minEnergySumOnPath = Double.POSITIVE_INFINITY;
        int[] seam = null;

        for (int x = 0; x < width(); ++ x) {

            int[] seamOnPath = new int[height()];
            double energyOnPath = 0.;
            seamOnPath[0] = x;

            for (int y = 0; y < height() - 1; ++y) {

                double minEnergy = Double.POSITIVE_INFINITY;
                int minEnergyPos = 0;
                for (Integer[] pixelPos: getNextPixelSet(x, y)) {
                    if (getEnergy(pixelPos[0], pixelPos[1]) < minEnergy) {
                        minEnergy = getEnergy(pixelPos[0], pixelPos[1]);
                        minEnergyPos = pixelPos[0];
                    }
                }

                energyOnPath += minEnergy;
                seamOnPath[y + 1] = minEnergyPos;

            }

            if (energyOnPath < minEnergySumOnPath) {
                seam = seamOnPath;
                minEnergySumOnPath = energyOnPath;
            }
        }

        return seam;
    }

    public void removeHorizontalSeam(int[] seam) {

    }

    public void removeVerticalSeam(int[] seam) {

        Picture seamCarvedImage = new Picture(width() - 1, height());
        for (int y = 0; y < height(); ++y) {
            int count = 0;
            for (int x = 0; x < width(); ++ x) {
                if (x != seam[y]) {
                    seamCarvedImage.setRGB(count, y, picture().getRGB(x, y));
                    count += 1;
                }
            }
        }
    }

    private void calculateEnergyArray() {
        initEnergyArray();

        for (int x = 0; x < width(); ++x) {
            for (int y = 0; y < height(); ++y) {
                energyArray[x][y] = getEnergy(x, y);
            }
        }
    }

    private void transposeEnergyMap() {
        double[][] energyTranspose = new double[height()][];

        for (int y = 0; y < height(); ++y) {
            energyTranspose[y] = new double[width()];

            for (int x = 0; x < width(); ++x) {
                energyTranspose[y][x] = energyArray[x][y];
            }
        }

        energyArray = energyTranspose;
    }

    private double getEnergy(int x, int y) {
        if (isBoundary(x, y)) {
            return BOUNDARY_ENERGY;
        }

        double xGrad = getGradSquare(x, y, true);
        double yGrad = getGradSquare(x, y, false);

        return Math.sqrt(xGrad + yGrad);
    }

    private double getGradSquare(int x, int y, boolean isXDir) {

        int[] rgbNext,rgbPrev;
        if (isXDir) {
            rgbNext = getRGB(x + 1, y);
            rgbPrev = getRGB(x - 1, y);
        } else {
            rgbNext = getRGB(x, y + 1);
            rgbPrev = getRGB(x, y - 1);
        }

        int sum = 0;
        for (int c = 0; c < 3; ++c) {
            sum += (rgbNext[c] - rgbPrev[c]) * (rgbNext[c] - rgbPrev[c]);
        }

        return (double) sum;
    }

    private void initEnergyArray() {
        energyArray = new double[width()][];

        for (int x = 0; x < width(); ++x) {
            energyArray[x] = new double[height()];
        }
    }

    private boolean isBoundary(int x, int y) {
        return x == 0 || x == width() - 1 || y ==0 || y == height() - 1;
    }

    private int[] getRGB(int x, int y) {
        int color = image.getRGB(x, y);
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;

        return new int[]{red, green, blue};
    }

    private Iterable<Integer[]> getNextPixelSet(int x, int y) {
        ArrayList<Integer[]> neighbors = new ArrayList<>();

        y += 1;
        for (int xShift = x -1; xShift <= x + 1; ++xShift) {
            if (!isOutBound(xShift, y)) {
                neighbors.add(new Integer[]{xShift, y});
            }
        }
        return  neighbors;
    }

    private boolean isOutBound(int x, int y) {
        return x < 0 || x >= width() || y < 0 || y >= height();
    }

    public static void main(String[] args) {
        Picture p = new Picture("data/img/HJocean.png");
        SeamCarver seamCarver = new SeamCarver(p);
    }
}
