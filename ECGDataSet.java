
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * class ECGDataSet - describes a set of data gathered from a single channel
 *
 * @author Dakota Williams
 */
public class ECGDataSet implements Undoable {
	private List<double[]> set;
	private boolean bad;

	/**
	 * Constructor - initializes an ECGDataSet
	 */
	public ECGDataSet() {
		set = new ArrayList<double[]>();
	}

	/**
	 * isBad - is the bad lead flag set?
	 *
	 * @return true if the bad lead flag is set, false otherwise
	 */
	public boolean isBad() {return bad;}

	/**
	 * setBad - sets the bad lead flag
	 *
	 * @param b which value to set the bad lead flag to
	 */
	public void setBad(boolean b) {bad = b;}

	/**
	 * addTuple - adds an entry to the end of the dataset
	 *
	 * @param x the time at which the sample occurs
	 * @param y the value of the sample
	 */
	public void addTuple(double x, double y) {
		set.add(new double[] {x, y});
	}

	/**
	 * getAt - gets the nth sample
	 * 
	 * @return a 2-element array containing: [0] - the time
	 *					[1] - the value
	 */
	public double[] getAt(int index) {
		return set.get(index);
	}

	/**
	 * size - gets the number of samples in the set
	 *
	 * @return the number of samples in the set
	 */
	public int size() {
		return set.size();
	}

	/**
	 * clone - deep copy of the dataset
	 *
	 * @return the new dataset
	 */
	public Object clone() {
		ECGDataSet eds = new ECGDataSet();
		eds.set = new ArrayList<double[]>();
		for(int i = 0; i < this.set.size(); i++) {
			eds.set.add(new double[] {(double)this.set.get(i)[0],
									  (double)this.set.get(i)[1]});
		}
		eds.bad = this.bad;
		return eds;
	}

	/**
	 * copyFrom - make this a shallow copy of e
	 *
	 * @param e the thing to copy
	 */
	public void copyFrom(ECGDataSet e) {
		this.set = new ArrayList<double[]>(e.set);
		this.bad = e.bad;
	}

	/**
	 * toArray - creates an array representation of the dataset
	 *
	 * @return a 2xN matrix where the array of arrays is the x and y values
	 */
	public double[][] toArray() {
		double[][] ret = new double[2][set.size()];

		for(int j = 0; j < set.size(); j++) {
			//do a transpose
			ret[0][j] = set.get(j)[0];
			ret[1][j] = set.get(j)[1];
		}
		return ret;
	}

	/**
	 * subset - gets a subset of this dataset
	 *
	 * @param start the time before the first element in the subset
	 * @param end the time after the last element in the subset
	 * @return the new dataset that contains the subset
	 */
	public ECGDataSet subset(double start, double end) {
		ECGDataSet newSet = new ECGDataSet();

		for(int i = 0; i < this.set.size(); i++) {
			if(this.set.get(i)[0] >= start && this.set.get(i)[0] < end) {
				newSet.set.add(this.set.get(i));
			}
		}

		return newSet;
	}

	/**
	 * indexBefore - gets the index before a certain time
	 *
	 * @param time the time to find
	 * @return the index of an element directly before that time
	 */
	public int indexBefore(double time) {
		int min = 0;
		int max = set.size() - 1;

		while(max >= min) {
			int mid = (max + min)/2;
			if((set.get(mid)[0] <= time) && 
			   (set.get(mid+1)[0] == time)) {
				return mid+1;
			}
			else if(set.get(mid)[0] < time) {
				min = mid+1;
			}
			else {
				max = mid-1;
			}
		}
		return set.size() - 1;
	}

	/**
	 * detrend - applies a polynomial fit detrending on the dataset
	 *
	 * @param detrendPolynomial the degree of the fitting polynomial
	 */
	public void detrend(int detrendPolynomial) {
		Filters.detrend(set, detrendPolynomial);
	}

	/**
	 * sgolayfilt - applies a savitzky-golay filter to the dataset
	 *
	 * @param left number of elements to the left to sample
	 * @param right number of elements to the right to sample
	 * @param degree the degree of the polynomial to use
	 */
	public void sgolayfilt(int left, int right, int degree) {
		Filters.sgolayfilt(set, left, right, degree);
	}

	/**
	 * lowpassfilt - applies a low pass filter to the dataset
	 *
	 * @param freq the frequency threshold
	 */
	public void lowpassfilt(double freq) {
		Filters.lowpassfilt(set, freq);
	}

	/**
	 * highpassfilt - applies a high pass filter to the dataset
	 *
	 * @param freq the frequency threshold
	 */
	public void highpassfilt(double freq) {
		Filters.highpassfilt(set, freq);
	}

	/**
	 * highpassfftfilt - applies a fft filter to the dataset
	 *
	 * @param lowfreq the low frequency cut off
	 * @param highfreq the high frequency cut off
	 */
	public void highpassfftfilt(double lowfreq, double highfreq) {
		Filters.highpassfftfilt(set, lowfreq, highfreq);
	}

	/**
	 * waveletfilt - applies a wavelet filter to the dataset
	 *
	 * @param threshold the threshold value to cut off
	 */
	public void waveletfilt(double threshold, int wavelet, int level) {
		Filters.waveletfilt(set, threshold, wavelet, level);
	}

	/**
	 * constofffilt - applies a constant offset to the dataset
	 *
	 * @param offset the offset value
	 */
	public void constofffilt(double offset) {
		Filters.constofffilt(set, offset);
	}

	/**
	 * butterworthfilt - applies a butterworth filter to the dataset
	 *
	 * @param mode lpf, hpf, bpf
	 * @param rate the sample rate
	 * @param freq the cutoff frequency
	 * @param gain the signal gain
	 * @param order the order of the butterworth filter
	 */
	public void butterworthfilt(int mode, double rate, double freq, int order) {
		Filters.butterworthfilt(set, mode, rate, freq, order);
	}

	public void harmonicDetrend() {
		Filters.harmonicDetrend(set);
	}

	public void medianDetrend() {
		Filters.medianDetrend(set);
	}
}
