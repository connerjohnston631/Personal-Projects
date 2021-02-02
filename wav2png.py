from argparse import ArgumentParser
from wave import open as w_open 
from math import floor, pow as fpow
from itertools import count

from tqdm import tqdm
#from scipy.io.wavfile import read as w_read
import numpy as np
from PIL import Image
from colorsys import hls_to_rgb
from random import random

WINDOW_SIZE_DEFAULT = int(44100/10)
WINDOW_OVERLAP_DEFAULT = 0
HUE_HIGH_DEFAULT = 0   #for high values
HUE_LOW_DEFAULT = 270 #for low values
LIGHT_LOW_DEFAULT = 0.2
LIGHT_HIGH_DEFAULT = 0.65
FREQUENCY_DECAY_DEFAULT = 400
WIDTH_DEFAULT = 1000
HEIGHT_DEFAULT = 800

#flips n-dimensional array on vertical axis
#returns "view" of array
def vertical_flip(arr):
	return arr[::-1]
	
#maps 2-d array to rgb grid
def hue_amplitude_light_intensity(amplitude_arr, hue_low, hue_high, light_low, light_high):

	hue_low_normalized = hue_low / 360
	hue_high_normalized = hue_high / 360
	
	light_low_normalized = min(1., max(light_low, 0.))
	light_high_normalized = min(1., max(light_high, 0.))
	
	hue_mapping = {}
	
	sorted_1d_amplitude = amplitude_arr.flatten()
	sorted_1d_amplitude.sort()
	
	size = sorted_1d_amplitude.shape[0]
	delta = (hue_low_normalized - hue_high_normalized) / size

	for index in range(size):
		hue_mapping[sorted_1d_amplitude[index]] = hue_low_normalized - (index * delta)
		
	intensity_arr = amplitude_to_intensity(amplitude_arr)
	light_mapping = {}
	sorted_1d_intensity = intensity_arr.flatten()
	sorted_1d_intensity.sort()
	size = sorted_1d_intensity.shape[0]
	delta = (light_high_normalized - light_low_normalized) / size
	for index in range(size):
		light_mapping[sorted_1d_intensity[index]] = light_low_normalized + (index * delta)

	result = np.empty(amplitude_arr.shape + (3,), np.uint8)
	for row in range(amplitude_arr.shape[0]):
		for col in range(amplitude_arr.shape[1]):
			rgb = np.array([x * 255 for x in hls_to_rgb(hue_mapping[amplitude_arr[row][col]], light_mapping[intensity_arr[row][col]], 1.0)], dtype=np.uint8)
			result[row][col] = rgb
	return result
	
#TODO: figure out more elegant solution
def amplitude_to_intensity(arr):
	result = np.empty(arr.shape, arr.dtype)
	for row in range(arr.shape[0]):
		for col in range(arr.shape[1]):
			result[row][col] = (arr[row][col] ** 2) * row
	return result

	
def decay(x, half_life):
	return fpow(0.5, (float(x) / float(half_life)))
	
def normalize_fft(channel, rows, cols, window_size, delta, maxv):
  
	arr = np.empty((rows, cols), dtype=np.float64)
  
	for index in range(rows):
		start = index * delta
		buffer = channel[start:start+window_size]
		if len(buffer) < window_size:
			break
		normalized_buffer = np.fromiter(map(lambda x: x/maxv, buffer), np.float64)
		amplitudes = np.fromiter(map(abs, np.fft.rfft(normalized_buffer)), np.float64)
		arr[index] = amplitudes
	return arr
	
def wav_to_image(in_wav_file, out_png_file, window_size=WINDOW_SIZE_DEFAULT,
  window_overlap=WINDOW_OVERLAP_DEFAULT, hue_low_0=HUE_LOW_DEFAULT, hue_low_1=HUE_LOW_DEFAULT,
  hue_high_0=HUE_HIGH_DEFAULT, hue_high_1=HUE_HIGH_DEFAULT, light_low_0=LIGHT_LOW_DEFAULT,
  light_low_1=LIGHT_LOW_DEFAULT, light_high_0=LIGHT_HIGH_DEFAULT, light_high_1=LIGHT_HIGH_DEFAULT,
  frequency_decay=FREQUENCY_DECAY_DEFAULT, width=WIDTH_DEFAULT, height=HEIGHT_DEFAULT):
	
	
	#sample_rate, data = w_read(in_wav_file)
	with w_open(in_wav_file, 'rb') as wav_in:
		print(wav_in.getparams())
		
		#initialize array for image creation
		frames = wav_in.getnframes()
		delta = window_size - window_overlap
		rows = floor(frames / delta)- 1
		cols = (window_size // 2) + 1
		
		norm_val = np.iinfo(np.int16).max
		
		full_buffer = np.frombuffer(wav_in.readframes(frames), dtype=np.int16)
		channel_0 = full_buffer[0::2]
		channel_1 = full_buffer[1::2]
		
		n_fft_0 = normalize_fft(channel_0, rows, cols, window_size, delta, norm_val)
		n_fft_1 = normalize_fft(channel_1, rows, cols, window_size, delta, norm_val)
		
		mask = np.ones(cols, dtype=bool)
		for index in range(len(mask)):
			if random() > decay(index, frequency_decay):
				mask[index] = False
		
		n_fft_t_0 = np.transpose(n_fft_0)
		n_fft_t_1 = np.transpose(n_fft_1)
		
		decay_png_0 = n_fft_t_0[mask]
		decay_png_1 = n_fft_t_1[mask]
		
		png_arr_0 = hue_amplitude_light_intensity(decay_png_0, hue_low_0, hue_high_0, light_low_0, light_high_0)
		png_arr_1 = hue_amplitude_light_intensity(decay_png_1, hue_low_1, hue_high_1, light_low_1, light_high_1)
		
		png_arr_flip_0 = vertical_flip(png_arr_0)
		png_arr_flip_1 = vertical_flip(png_arr_1)
		print(png_arr_flip_0.shape)
		img0 = Image.fromarray(png_arr_flip_0)
		img1 = Image.fromarray(png_arr_flip_1)

		img = img0 #Image.blend(img0, img1, 0.5)
		img = img.resize((width, height), Image.ANTIALIAS)
		img.save(out_png_file)
	return


if __name__ == '__main__':
	#eval_func = lambda *args, **kwargs: print(args, kwargs)

	parser = ArgumentParser()
	parser.add_argument('input')
	parser.add_argument('output')
	parser.add_argument('--window-size', type=int, default=WINDOW_SIZE_DEFAULT)
	parser.add_argument('--window-overlap', type=int, default=WINDOW_OVERLAP_DEFAULT)
	parser.add_argument('--hue-low-0', type=float, default=HUE_LOW_DEFAULT)
	parser.add_argument('--hue-low-1', type=float, default=HUE_LOW_DEFAULT)
	parser.add_argument('--hue-high-0', type=float, default=HUE_HIGH_DEFAULT)
	parser.add_argument('--hue-high-1', type=float, default=HUE_HIGH_DEFAULT)
	parser.add_argument('--light-low-0', type=float, default=LIGHT_LOW_DEFAULT)
	parser.add_argument('--light-low-1', type=float, default=LIGHT_LOW_DEFAULT)
	parser.add_argument('--light-high-0', type=float, default=LIGHT_HIGH_DEFAULT)
	parser.add_argument('--light-high-1', type=float, default=LIGHT_HIGH_DEFAULT)
	parser.add_argument('--frequency-decay', type=float, default=FREQUENCY_DECAY_DEFAULT)
	parser.add_argument('--width', type=int, default=WIDTH_DEFAULT)
	parser.add_argument('--height', type=int, default=HEIGHT_DEFAULT)
	args = parser.parse_args()

	wav_to_image(args.input, args.output, window_size=args.window_size, window_overlap=args.window_overlap,
	  hue_low_0=args.hue_low_0, hue_low_1=args.hue_low_1, hue_high_0=args.hue_high_0, hue_high_1=args.hue_high_1,
	  light_low_0=args.light_low_0, light_low_1=args.light_low_1, light_high_0=args.light_high_0,
	  light_high_1=args.light_high_1, frequency_decay=args.frequency_decay, width=args.width, height=args.height)
