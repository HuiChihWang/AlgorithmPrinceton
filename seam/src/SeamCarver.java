import edu.princeton.cs.algs4.Picture;
import java.util.ArrayList;


public class SeamCarver {
    private Integer[][] image;
    private Double[][] energyArray;
    private boolean isMatrixNoTranspose = true;

    public SeamCarver(Picture picture) {
        checkObjectNull(picture);
        image = createImageArray(picture);
        calculateEnergyArray();
    }

    public Picture picture() {
        return isMatrixNoTranspose ? createPicture(image) : createPicture(transposeIntegerArray(image));
    }

    public int width() {
        return isMatrixNoTranspose ? image.length : image[0].length;
    }

    public int height() {
        return isMatrixNoTranspose ? image[0].length : image.length;
    }

    public  double energy(int x, int y) {
        checkOutBoundException(x, y);
        return isMatrixNoTranspose ? energyArray[x][y] : energyArray[y][x];
    }

    public int[] findVerticalSeam() {
        if (!isMatrixNoTranspose) {
            image = transposeIntegerArray(image);
            energyArray = transposeDoubleArray(energyArray);
            isMatrixNoTranspose = true;
        }

        return findSeamOnImage();
    }

    public int[] findHorizontalSeam() {
        if (isMatrixNoTranspose) {
            image = transposeIntegerArray(image);
            energyArray = transposeDoubleArray(energyArray);
            isMatrixNoTranspose = false;
        }

        return findSeamOnImage();
    }

    public void removeHorizontalSeam(int[] seam) {
        if (isMatrixNoTranspose) {
            image = transposeIntegerArray(image);
            energyArray = transposeDoubleArray(energyArray);
            isMatrixNoTranspose = false;
        }

        image = removeSeamOnImage(seam);
        calculateEnergyArray();

    }

    public void removeVerticalSeam(int[] seam) {

        if (!isMatrixNoTranspose) {
            image = transposeIntegerArray(image);
            energyArray = transposeDoubleArray(energyArray);
            isMatrixNoTranspose = true;
        }

        image = removeSeamOnImage(seam);
        calculateEnergyArray();
    }

    private Integer[][] removeSeamOnImage(int[] seam) {
        checkValidSeam(seam);
        Integer[][] carvedImage =  createEmpty2DIntegerArray(image.length - 1, image[0].length);

        for (int y = 0; y < image[0].length; ++y) {
            int countX = 0;
            for (int x = 0; x < image.length; ++ x) {
                if (x != seam[y]) {
                    carvedImage[countX][y] = image[x][y];
                    countX += 1;
                }
            }
        }

        return carvedImage;
    }

    private int[] findSeamOnImage() {
        Double[][] energyAcc = createEmpty2DDoubleArray(image.length, image[0].length);
        Integer[][] prevLocation = createEmpty2DIntegerArray(image.length, image[0].length);

        for (int x = 0; x < image.length; ++x) {
            for (int y = 0; y < image[0].length; ++y) {
                energyAcc[x][y] = energyArray[x][y];
            }
        }

        for (int y = 1; y < image[0].length; ++y) {
            for (int x = 0; x < image.length; ++x) {
                int prevShift = findPreviousMinEnergyPos(x, y, energyAcc);
                energyAcc[x][y] += energyAcc[prevShift][y - 1];
                prevLocation[x][y] = prevShift;
            }
        }

        return findPathWithMinEnergy(energyAcc, prevLocation);
    }

    private int findPreviousMinEnergyPos(int x, int y, Double[][] energyAcc) {
        int minEnergyPos = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (Integer[] prevPos: getPrevPixelSet(x, y)) {
            if (energyAcc[prevPos[0]][prevPos[1]] < minEnergy) {
                minEnergyPos = prevPos[0];
                minEnergy = energyAcc[prevPos[0]][prevPos[1]];
            }
        }

        return minEnergyPos;
    }

    private int[] findPathWithMinEnergy(Double[][] energyAcc, Integer[][] prevLocation) {
        int minIdx = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int x = 0; x < image.length; ++x) {
            if (energyAcc[x][image[0].length-1] < minEnergy) {
                minEnergy = energyAcc[x][image[0].length - 1];
                minIdx = x;
            }
        }

        int[] seam = new int[image[0].length];
        seam[image[0].length-1] = minIdx;

        for (int y = image[0].length-2; y >= 0; --y) {
            seam[y] = prevLocation[seam[y+1]][y+1];
        }

        return seam;
    }

    private Iterable<Integer[]> getPrevPixelSet(int x, int y) {
        ArrayList<Integer[]> neighbors = new ArrayList<>();

        y -= 1;
        for (int xShift = x - 1; xShift <= x + 1; ++xShift) {
            if (!isOutBound(xShift, y)) {
                neighbors.add(new Integer[]{xShift, y});
            }
        }
        return  neighbors;
    }


    private void calculateEnergyArray() {
        energyArray = createEmpty2DDoubleArray(image.length, image[0].length);

        for (int x = 0; x < image.length; ++x) {
            for (int y = 0; y < image[0].length; ++y) {
                energyArray[x][y] = getEnergy(x, y);
            }
        }
    }

    private Integer[][] createImageArray(Picture picture) {
        Integer[][] imgArray = createEmpty2DIntegerArray(picture.width(), picture.height());

        for (int x = 0; x < picture.width(); ++x) {
            for (int y = 0; y < picture.height(); ++y) {
                imgArray[x][y] = picture.getRGB(x, y);
            }
        }
        return imgArray;
    }

    private Picture createPicture(Integer[][] imageArray) {
        Picture picture = new Picture(imageArray.length, imageArray[0].length);

        for (int x = 0; x < imageArray.length; ++x) {
            for (int y = 0; y < imageArray[0].length; ++y) {
                picture.setRGB(x, y, imageArray[x][y]);
            }
        }
        return picture;
    }

    private double getEnergy(int x, int y) {
        if (isBoundary(x, y)) {
            return 1000.;
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

    private boolean isBoundary(int x, int y) {
        return x == 0 || x == image.length - 1 || y == 0 || y == image[0].length - 1;
    }

    private int[] getRGB(int x, int y) {
        int color = image[x][y];
        int blue = color & 0xff;
        int green = (color & 0xff00) >> 8;
        int red = (color & 0xff0000) >> 16;

        return new int[]{red, green, blue};
    }

    private boolean isOutBound(int x, int y) {
        return x < 0 || x >= image.length || y < 0 || y >= image[0].length;
    }

    private static Integer[][] createEmpty2DIntegerArray(int rowNum, int colNum) {
        Integer[][] newArray = new Integer[rowNum][];
        for (int row = 0; row < rowNum; ++row) {
            newArray[row] = new Integer[colNum];
        }
        return newArray;
    }

    private static Double[][] createEmpty2DDoubleArray(int rowNum, int colNum) {
        Double[][] newArray = new Double[rowNum][];
        for (int row = 0; row < rowNum; ++row) {
            newArray[row] = new Double[colNum];
        }
        return newArray;
    }

    private static Integer[][] transposeIntegerArray(Integer[][] array) {
        int rowNum = array.length;
        int colNum = array[0].length;
        Integer[][] arrayTranspose = new Integer[colNum][];

        for (int colIdx = 0; colIdx < colNum; ++colIdx) {
            arrayTranspose[colIdx] = new Integer[rowNum];

            for (int rowIdx = 0; rowIdx < rowNum; ++rowIdx) {
                arrayTranspose[colIdx][rowIdx] = array[rowIdx][colIdx];
            }
        }

        return arrayTranspose;
    }

    private static Double[][] transposeDoubleArray(Double[][] array) {
        int rowNum = array.length;
        int colNum = array[0].length;
        Double[][] arrayTranspose = new Double[colNum][];

        for (int colIdx = 0; colIdx < colNum; ++colIdx) {
            arrayTranspose[colIdx] = new Double[rowNum];

            for (int rowIdx = 0; rowIdx < rowNum; ++rowIdx) {
                arrayTranspose[colIdx][rowIdx] = array[rowIdx][colIdx];
            }
        }

        return arrayTranspose;
    }

    private void checkOutBoundException(int x, int y) {
        if (x  < 0 || y < 0 || x >= width() || y>= height()) {
            throw new IllegalArgumentException();
        }
    }

    private void checkObjectNull(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    private void checkValidSeam(int[] seam) {
        checkObjectNull(seam);

        if (image.length <= 1) {
            throw new IllegalArgumentException();
        }

        if (seam.length != image[0].length) {
            throw new IllegalArgumentException();
        }

        int prevIdx = seam[0];
        for (int idx: seam){
            if (idx < 0 || idx >= image.length) {
                throw new IllegalArgumentException();
            }

            if (Math.abs(prevIdx - idx) > 1) {
                throw new IllegalArgumentException();
            }

            prevIdx = idx;
        }
    }
}
