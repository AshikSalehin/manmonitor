(function () {
    //Your_Name
    'use strict'
    angular.module('manmon')
        .factory("manmonDataPreparationService", manmonDataPreparationService);
    function manmonDataPreparationService() {
        var manmonDataServices = {
            mockTempArray: mockTempArray,
            mockNoiseArray: mockNoiseArray,
            mockHumArray: mockHumArray,
            mockLightArray: mockLightArray,
            mockPreviousDaysMinMax: mockPreviousDaysMinMax,
            getCurrentArray: getCurrentArray,
            getPreviousArray: getPreviousArray,
            getEarlierArray: getEarlierArray
        };

        function getCurrentArray(initial15MinuteSensorReadings, sensorValue) {
            var currentSensorDataArray = [];
            if (sensorValue == 'temperature') {
                currentSensorDataArray.push(['Time', 'Temperature']);
            }
            else if (sensorValue == 'humidity') {
                currentSensorDataArray.push(['Time', 'Humidity']);
            }
            else if (sensorValue == 'light') {
                currentSensorDataArray.push(['Time', 'light']);
            }
            else if (sensorValue == 'noise') {
                currentSensorDataArray.push([' ', 'MinNoise', 'MaxNoise', 'NoiseLevel']);
            }


            var i;
            var z = 90;
            if (initial15MinuteSensorReadings.length < z) { z = initial15MinuteSensorReadings.length; }
            for (i = 60; i <= z - 1; i++) {
                var readingTime = initial15MinuteSensorReadings[i].time[10] + initial15MinuteSensorReadings[i].time[11] + ':' + initial15MinuteSensorReadings[i].time[12] + initial15MinuteSensorReadings[i].time[13];
                if (sensorValue == 'temperature') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].temperature)]);
                }
                else if (sensorValue == 'humidity') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].humidity)]);
                }
                else if (sensorValue == 'light') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].light)]);
                }
                else if (sensorValue == 'noise') {
                    currentSensorDataArray.push([" ", parseFloat(initial15MinuteSensorReadings[i].noiseMin), parseFloat(initial15MinuteSensorReadings[i].noiseMax), parseFloat(initial15MinuteSensorReadings[i].noiseLevel)]);
                }
            }
            return currentSensorDataArray;
        }

        function getPreviousArray(initial15MinuteSensorReadings, sensorValue) {
            var currentSensorDataArray = [];

            if (sensorValue == 'temperature') {
                currentSensorDataArray.push(['Time', 'Temperature']);
            }
            else if (sensorValue == 'humidity') {
                currentSensorDataArray.push(['Time', 'Humidity']);
            }
            else if (sensorValue == 'light') {
                currentSensorDataArray.push(['Time', 'light']);
            }
            else if (sensorValue == 'noise') {
                currentSensorDataArray.push([' ', 'MinNoise', 'MaxNoise', 'NoiseLevel']);
            }

            var i;
            var z = 60;
            if (initial15MinuteSensorReadings.length < z) { z = initial15MinuteSensorReadings.length; }
            for (i = 30; i < z - 1; i++) {
                var readingTime = initial15MinuteSensorReadings[i].time[10] + initial15MinuteSensorReadings[i].time[11] + ':' + initial15MinuteSensorReadings[i].time[12] + initial15MinuteSensorReadings[i].time[13];
                if (sensorValue == 'temperature') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].temperature)]);
                }
                else if (sensorValue == 'humidity') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].humidity)]);
                }
                else if (sensorValue == 'light') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].light)]);
                }
                else if (sensorValue == 'noise') {
                    currentSensorDataArray.push([" ", parseFloat(initial15MinuteSensorReadings[i].noiseMin), parseFloat(initial15MinuteSensorReadings[i].noiseMax), parseFloat(initial15MinuteSensorReadings[i].noiseLevel)]);
                }
            }
            return currentSensorDataArray;
        }

        function getEarlierArray(initial15MinuteSensorReadings, sensorValue) {
            var currentSensorDataArray = [];

            if (sensorValue == 'temperature') {
                currentSensorDataArray.push(['Time', 'Temperature']);
            }
            else if (sensorValue == 'humidity') {
                currentSensorDataArray.push(['Time', 'Humidity']);
            }
            else if (sensorValue == 'light') {
                currentSensorDataArray.push(['Time', 'light']);
            }
            else if (sensorValue == 'noise') {
                currentSensorDataArray.push([' ', 'MinNoise', 'MaxNoise', 'NoiseLevel']);
            }

            var i;
            var z = 30;
            if (initial15MinuteSensorReadings.length < z) { z = initial15MinuteSensorReadings.length; }
            for (i = 0; i < z - 1; i++) {
                var readingTime = initial15MinuteSensorReadings[i].time[10] + initial15MinuteSensorReadings[i].time[11] + ':' + initial15MinuteSensorReadings[i].time[12] + initial15MinuteSensorReadings[i].time[13];
                if (sensorValue == 'temperature') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].temperature)]);
                }
                else if (sensorValue == 'humidity') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].humidity)]);
                }
                else if (sensorValue == 'light') {
                    currentSensorDataArray.push([readingTime, parseFloat(initial15MinuteSensorReadings[i].light)]);
                }
                else if (sensorValue == 'noise') {
                    currentSensorDataArray.push([" ", parseFloat(initial15MinuteSensorReadings[i].noiseMin), parseFloat(initial15MinuteSensorReadings[i].noiseMax), parseFloat(initial15MinuteSensorReadings[i].noiseLevel)]);
                }
            }
            return currentSensorDataArray;
        }

        function mockTempArray() {
            var tempArray = [
                ['Time', 'Temperature'],
                ['20:04', 15],
                ['20:05', 9],
                ['20:06', 21.5],
                ['20:07', 12.3],
                ['20:08', 18],
                ['20:09', 11],
                ['20:10', 10],
                ['20:11', 16],
                ['20:12', 11],
                ['20:13', 18.7],
                ['20:14', 20],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:04', 10],
                ['20:05', 21],
                ['20:06', 11.5],
                ['20:07', 17],
                ['20:08', 12],
                ['20:09', 9],
                ['20:10', 13],
                ['20:11', 17],
                ['20:12', 16],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5]
            ];
            return tempArray;
        }

        function mockNoiseArray() {
            var noiseArray = [
                ['Time', 'MinNoise', 'MaxNoise'],
                ['20:04', 1, 0],
                ['20:05', 1, 0],
                ['20:06', 1, 0],
                ['20:07', 1, 0],
                ['20:08', 1, 0],
                ['20:09', 1, 0],
                ['20:10', 1, 0],
                ['20:11', 1, 0],
                ['20:12', 1, 0],
                ['20:13', 1, 0],
                ['20:14', 1, 0],
                ['20:15', 1, 0],
                ['20:16', 1, 0],
                ['20:04', 1, 0],
                ['20:05', 1, 0],
                ['20:06', 1, 0],
                ['20:07', 1, 0],
                ['20:08', 1, 0],
                ['20:09', 1, 0],
                ['20:10', 1, 0],
                ['20:11', 1, 0],
                ['20:12', 1, 0],
                ['20:13', 1, 0],
                ['20:14', 1, 0],
                ['20:15', 1, 0],
                ['20:16', 1, 0],
                ['20:13', 1, 0],
                ['20:14', 1, 0],
                ['20:15', 1, 0],
                ['20:16', 1, 0]
            ];
            return noiseArray;
        }
        function mockHumArray() {
            var humArray = [
                ['Time', 'Humidity'],
                ['20:04', 10],
                ['20:05', 11],
                ['20:06', 11.5],
                ['20:07', 12.3],
                ['20:08', 12],
                ['20:09', 9],
                ['20:10', 13],
                ['20:11', 17],
                ['20:12', 16],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:04', 10],
                ['20:05', 11],
                ['20:06', 11.5],
                ['20:07', 12.3],
                ['20:08', 12],
                ['20:09', 9],
                ['20:10', 13],
                ['20:11', 17],
                ['20:12', 16],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5]
            ];
            return humArray;
        }

        function mockLightArray() {
            var lightArray = [
                ['Time', 'Light Intensity'],
                ['20:04', 5],
                ['20:05', 11],
                ['20:06', 20],
                ['20:07', 12.3],
                ['20:08', 7],
                ['20:09', 9],
                ['20:10', 13],
                ['20:11', 17],
                ['20:12', 16],
                ['20:13', 15],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:04', 16],
                ['20:05', 11],
                ['20:06', 11.5],
                ['20:07', 6],
                ['20:08', 12],
                ['20:09', 9],
                ['20:10', 10],
                ['20:11', 17],
                ['20:12', 16],
                ['20:13', 11.7],
                ['20:14', 19],
                ['20:15', 11],
                ['20:16', 10.5],
                ['20:13', 11.7],
                ['20:14', 12],
                ['20:15', 11],
                ['20:16', 10.5]
            ];
            return lightArray;
        }

        function mockPreviousDaysMinMax() {
            var previousDaysMinMax = [
                {
                    'time': '20200925',
                    'temperature': {
                        'min': 11,
                        'max': 25
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 11,
                        'max': 13
                    }
                },
                {
                    'time': '20200924',
                    'temperature': {
                        'min': 11,
                        'max': 14
                    },
                    'humidity': {
                        'min': 12,
                        'max': 14
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200923',
                    'temperature': {
                        'min': 12,
                        'max': 14
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200922',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200921',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200920',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200919',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200918',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200917',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200916',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200915',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200914',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200913',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200912',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200911',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                },
                {
                    'time': '20200910',
                    'temperature': {
                        'min': 12,
                        'max': 50
                    },
                    'humidity': {
                        'min': 12,
                        'max': 50
                    },
                    'light': {
                        'min': 12,
                        'max': 50
                    }
                }
            ];
            return previousDaysMinMax;
        }


        return manmonDataServices;

    }
})();