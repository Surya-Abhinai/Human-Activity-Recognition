# Human-Activity-Recognition

Mentors :- 
* Surya Abhinai 
* Abhi Raj
* Aman Raj

Mentees :- 
* Radhika 

# Dataset

## Description of Experiment

The dataset was collected through experiments conducted with a group of 30 volunteers aged between 19 and 48 years. Each participant performed six activities (WALKING, WALKING_UPSTAIRS, WALKING_DOWNSTAIRS, SITTING, STANDING, LAYING) while wearing a Samsung Galaxy S II smartphone on their waist. The smartphone's embedded accelerometer and gyroscope were used to capture 3-axial linear acceleration and 3-axial angular velocity at a constant rate of 50Hz. The experiments were video-recorded for manual labeling. The dataset was randomly partitioned into training (70%) and test (30%) sets.

## Sensor Signal Pre-processing

The accelerometer and gyroscope signals underwent pre-processing, including the application of noise filters. The signals were then sampled in fixed-width sliding windows of 2.56 seconds with a 50% overlap (128 readings per window). The sensor acceleration signal, containing gravitational and body motion components, was separated using a Butterworth low-pass filter. The low-frequency components (assumed to be the gravitational force) were isolated using a filter with a 0.3 Hz cutoff frequency.

## Attribute Information

For each record in the dataset, the following information is provided:

- Triaxial acceleration from the accelerometer (total acceleration) and estimated body acceleration.
- Triaxial angular velocity from the gyroscope.
- A 561-feature vector with time and frequency domain variables.
- Activity label indicating one of the six performed activities.
- Subject identifier, representing the individual who carried out the experiment.

## User Interface

### Page 1

<img src="https://github.com/Surya-Abhinai/Human-Activity-Recognition/assets/119744527/4733954f-3eda-4d08-b959-5c0c607af90a" width="300">

<img src = "https://github.com/Surya-Abhinai/Human-Activity-Recognition/assets/119744527/2bdabb9d-0af5-4fd8-bd2a-b853b414aa4f" width="300">

<img src = "https://github.com/Surya-Abhinai/Human-Activity-Recognition/assets/119744527/f0c6bedf-ffc0-415a-bfaf-652fdddafa82" width="300">

### Page 2

<img src = "https://github.com/Surya-Abhinai/Human-Activity-Recognition/assets/119744527/d3ee9d2c-11cd-456f-ba98-f0236f8ade27" width = "300">

