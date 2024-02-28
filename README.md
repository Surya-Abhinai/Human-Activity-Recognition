# Human-Activity-Recognition

Mentors :- 
Surya Abhinai 
Abhi Raj 

Mentees :- 
Radhika 

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
