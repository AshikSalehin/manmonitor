(function () {
    //Md. Ashiqussalehin
    'use strict'
    angular.module('manmon')
        .controller("homeController", homeController);
    function homeController($scope, $interval, manmonDataPreparationService) {
        debugger
        var vm = this;
        vm.demoMode = 1;
        vm.timeToGraph = "00:00";
        vm.toViewController = false;
        vm.initiaSynchronization = true;
        $scope.temperature = 0;
        $scope.humidity = 0;
        $scope.ligntIntensity = 0;
        $scope.noiseLevel = 0;
        $scope.light = true;
        $scope.humidifier = true;
        var currentHumidityArray = [];

        init();

        function init() {
            fetchAllPreRepuiredData();
            getDateHourSpecificSensorData();
        }

        function fetchAllPreRepuiredData() {

            ///send http requrest to get these data
            var thresholdFromBackend = httpGet("http://192.168.2.105:8080/threshold");
            vm.initialThreshold = JSON.parse(thresholdFromBackend);
            var initial15MinuteSensorReadingsFromBackend = httpGet("http://192.168.2.105:8080/initial_sensorinfo");
            vm.dbFormat15MinuteSensorReadings = JSON.parse(initial15MinuteSensorReadingsFromBackend);
            var prepareInitial15MinuteSensorReadings = [];
            vm.dbFormat15MinuteSensorReadings.forEach(function (sensorInfo) {
                var sentTime = sensorInfo.time;
                var sentSensorInfoJson = JSON.parse(sensorInfo.sensorInfo);
                sentSensorInfoJson.time = sentTime;
                prepareInitial15MinuteSensorReadings.push(sentSensorInfoJson);
            });

            vm.initial15MinuteSensorReadings = prepareInitial15MinuteSensorReadings.reverse();

            var initial29DaysMinMaxFromBackend = httpGet("http://192.168.2.105:8080/current_date_min_max");
            var dbFormatinitial29DaysMinMax = JSON.parse(initial29DaysMinMaxFromBackend);
            vm.initial29DaysMinMax = [];
            dbFormatinitial29DaysMinMax.forEach(function (sensorMinMaxStatus) {
                var obj = {};
                obj.sensorMinMaxStatus = JSON.parse(sensorMinMaxStatus.sensorMinMaxStatus);
                vm.initial29DaysMinMax.push(obj);

            });
            vm.initial29DaysMinMax.reverse();
            vm.initialRelayStatus = vm.initial15MinuteSensorReadings[vm.initial15MinuteSensorReadings.length - 1].relayInfo;

            ///assign values
            vm.temperatureMinThreshold = vm.initialThreshold.temperatureMin;
            vm.temperatureMaxThreshold = vm.initialThreshold.temperatureMax;
            vm.humidityMinThreshold = vm.initialThreshold.humidityMin;
            vm.humidityMaxThreshold = vm.initialThreshold.humidityMax;
            vm.lightMinThreshold = vm.initialThreshold.lightMin;
            vm.lightMaxThreshold = vm.initialThreshold.lightMax;
            vm.currentTemperatureArray = manmonDataPreparationService.getCurrentArray(angular.copy(vm.initial15MinuteSensorReadings), 'temperature');
            vm.currentHumidityArray = manmonDataPreparationService.getCurrentArray(angular.copy(vm.initial15MinuteSensorReadings), 'humidity');
            vm.currentLightArray = manmonDataPreparationService.getCurrentArray(angular.copy(vm.initial15MinuteSensorReadings), 'light');
            vm.currentNoiseArray = manmonDataPreparationService.getCurrentArray(angular.copy(vm.initial15MinuteSensorReadings), 'noise');

            vm.previousTemperatureArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'temperature');
            vm.previousHumidityArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'humidity');
            vm.previousLightArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'light');
            vm.previousNoiseArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'noise');

            vm.earlierTemperatureArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'temperature');
            vm.earlierHumidityArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'humidity');
            vm.earlierLightArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'light');
            vm.earlierNoiseArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'noise');
        }


        function prepareAllArrayWithNewData(environmentStatus) {
            var updated15MinuteSensorReadings = [];
            var i = 0;
            vm.initial15MinuteSensorReadings.forEach(function (item) {
                if (i > 0) {
                    updated15MinuteSensorReadings.push(item);
                }
                i = i + 1;
            });
            environmentStatus.sensorInfo.time = environmentStatus.time;
            updated15MinuteSensorReadings.push(environmentStatus.sensorInfo);
            vm.initial15MinuteSensorReadings = updated15MinuteSensorReadings;

            vm.currentTemperatureArray = manmonDataPreparationService.getCurrentArray(vm.initial15MinuteSensorReadings, 'temperature');
            vm.currentHumidityArray = manmonDataPreparationService.getCurrentArray(vm.initial15MinuteSensorReadings, 'humidity');
            vm.currentLightArray = manmonDataPreparationService.getCurrentArray(vm.initial15MinuteSensorReadings, 'light');
            vm.currentNoiseArray = manmonDataPreparationService.getCurrentArray(vm.initial15MinuteSensorReadings, 'noise');

            vm.previousTemperatureArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'temperature');
            vm.previousHumidityArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'humidity');
            vm.previousLightArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'light');
            vm.previousNoiseArray = manmonDataPreparationService.getPreviousArray(vm.initial15MinuteSensorReadings, 'noise');

            vm.earlierTemperatureArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'temperature');
            vm.earlierHumidityArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'humidity');
            vm.earlierLightArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'light');
            vm.earlierNoiseArray = manmonDataPreparationService.getEarlierArray(vm.initial15MinuteSensorReadings, 'noise');

        }

        ///socket code
        var stompClient = null;
        var socket = null;
        function Sockconnect() {
            socket = new SockJS('/gs-guide-websocket');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                debugger;
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/sensorinfo', function (envStatus) {
                    var environmentStatus = JSON.parse(envStatus.body);
                    vm.currentMinMaxStatus = environmentStatus.sesnsorMinMaxInfo;
                    vm.currentMinMaxStatus.time = environmentStatus.time;
                    if (vm.initiaSynchronization) {
                        if (environmentStatus.relayInfo.replay01 == "1") {
                            $scope.light = true;
                        }
                        else {
                            $scope.light = false;
                        }
                        if (environmentStatus.relayInfo.replay02 == "1") {
                            $scope.humidifier = true;
                        }
                        else {
                            $scope.humidifier = false;
                        }
                        vm.initiaSynchronization = false;
                    }
                    if (environmentStatus.relayInfo.replay01 == "1") {
                        vm.lampSwitchStatus = true;
                    }
                    else {
                        vm.lampSwitchStatus = false;
                    }
                    if (environmentStatus.relayInfo.replay02 == "1") {
                        vm.dehumidifierSwitchStatus = true;
                    }
                    else {
                        vm.dehumidifierSwitchStatus = false;
                    }
                    if (environmentStatus.sensorInfo.noiseMin == "1") {
                        vm.noiseMin = 1;
                    }
                    else {
                        vm.noiseMin = 0;
                    }
                    if (environmentStatus.sensorInfo.noiseMax == "1") {
                        vm.noiseMax = 1;
                    }
                    else {
                        vm.noiseMax = 0;
                    }
                    prepareAllArrayWithNewData(environmentStatus);
                    $scope.temperature = parseFloat(environmentStatus.sensorInfo.temperature).toFixed(2);
                    $scope.humidity = parseFloat(environmentStatus.sensorInfo.humidity).toFixed(2);
                    $scope.ligntIntensity = parseFloat(environmentStatus.sensorInfo.light).toFixed(2);
                    var getTime = environmentStatus.time;
                    vm.timeToGraph = getTime.charAt(10) + getTime.charAt(11) + ":" + getTime.charAt(12) + getTime.charAt(13);

                    var sensorProcessedData = {};
                    sensorProcessedData.temperature = angular.copy($scope.temperature);
                    sensorProcessedData.humidity = angular.copy($scope.humidity);
                    sensorProcessedData.light = angular.copy($scope.ligntIntensity);
                    humidityGraph(null, vm.currentHumidityArray);
                    lightGraph(null, vm.currentLightArray);
                    temperatureGraph(null, vm.currentTemperatureArray);
                    noiseGraph(null, null, vm.currentNoiseArray);

                    currentTemperatureGraph(null, vm.currentTemperatureArray);
                    previousTemperatureGraph(null, vm.previousTemperatureArray);
                    earlierTemperatureGraph(null, vm.earlierTemperatureArray);

                    currentHumidityGraph(null, vm.currentHumidityArray);
                    previousHumidityGraph(null, vm.previousHumidityArray);
                    earlierHumidityGraph(null, vm.earlierHumidityArray);

                    currentNoiseGraph(null, null, vm.currentNoiseArray);
                    previousNoiseGraph(null, null, vm.previousNoiseArray);
                    earlierNoiseGraph(null, null, vm.earlierNoiseArray);

                    currentLightGraph(null, vm.currentLightArray);
                    previousLightGraph(null, vm.previousLightArray);
                    earlierLightGraph(null, vm.earlierLightArray);
                });

            });
            vm.toViewController = true;
        }

        //Sockconnect();

        var tempArray = manmonDataPreparationService.mockTempArray();
        var noiseArray = manmonDataPreparationService.mockNoiseArray();
        var humArray = manmonDataPreparationService.mockHumArray();
        var lightArray = manmonDataPreparationService.mockLightArray();
        vm.previousDaysMinMax = manmonDataPreparationService.mockPreviousDaysMinMax();

        ////        var ws = new WebSocket("ws://127.0.0.1:5678/");
        ////        ws.onmessage = function (event) {
        ////            var content = document.createTextNode(event.data);
        ////            currentHumidityArray.push(content.data);
        ////            $scope.humidityInfo = currentHumidityArray.join();
        ////            //$route.reload();
        ////            //console.log($scope.humidityInfo);
        ////            var mock = mokValueUpdate();
        ////            var getTime = mock.time;
        ////            //vm.timeToGraph = getTime.charAt(8) + getTime.charAt(9) + ":" + getTime.charAt(10) + getTime.charAt(11);
        ////            $scope.temperature = mock.temperature.toFixed(2);
        ////            $scope.humidity = mock.humidity.toFixed(2);
        ////            $scope.ligntIntensity = mock.light.toFixed(2);
        ////            temperatureGraph(mock, tempArray);
        ////            noiseGraph(1, 0, noiseArray);
        ////            humidityGraph(mock, humArray);
        ////            lightGraph(mock, lightArray);
        ////            currentTemperatureGraph(mock, tempArray);
        ////            previousTemperatureGraph(mock, tempArray);
        ////            earlierTemperatureGraph(mock, tempArray);
        ////            currentHumidityGraph(mock, humArray);
        ////            previousHumidityGraph(mock, humArray);
        ////            earlierHumidityGraph(mock, humArray);
        ////            currentNoiseGraph(1, 0, noiseArray);
        ////            previousNoiseGraph(1, 0, noiseArray);
        ////            earlierNoiseGraph(1, 0, noiseArray);
        ////            currentLightGraph(mock, lightArray);
        ////            previousLightGraph(mock, lightArray);
        ////            earlierLightGraph(mock, lightArray);
        ////
        ////            if (vm.toViewController == false) {
        ////                vm.toViewController = true;
        ////            }
        ////            //  sleep(10000);
        ////        };


        function temperatureGraph(newValue, allTemp) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allTemp;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.temperature);
                    oldArray = updatedArray
                    tempArray = oldArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);
                var options = {
                    vAxis: {
                        maxValue: 50,
                        minValue: 5
                        //gridlines:{interval:10}
                    },
                    hAxis: { gridlines: { interval: 30 } },
                    title: 'Temperature Review',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart = new google.visualization.LineChart(document.getElementById('curve_chart'));
                chart.draw(data, options);
            }

        }

        function noiseGraph(newMaxValue, newMinValue, allNoise) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allNoise;
                if (newMaxValue) {
                    var updatedArray = changeNoiseArrayQueue(oldArray, newMaxValue, newMinValue);
                    oldArray = updatedArray;
                    noiseArray = oldArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);
                var options = {
                    vAxis: {
                        maxValue: 2,
                        minValue: -2,
                        gridlines: { interval: 2 }
                    },
                    //hAxis: { gridlines: { interval: 4 } },
                    title: 'Noise Review',
                    curveType: 'none',
                    legend: { position: 'bottom' }
                };

                var chart = new google.visualization.LineChart(document.getElementById('curve_chart4'));
                chart.draw(data, options);
            }

        }

        function humidityGraph(newValue, allHum) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);
            var oldArray = allHum;
            if (newValue) {
                var updatedArray = changeArrayQueue(oldArray, newValue.humidity);
                oldArray = updatedArray
                humArray = oldArray;
            }
            function drawChart() {
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 100,
                        minValue: 5
                    },
                    title: 'Humidity Review',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart2 = new google.visualization.LineChart(document.getElementById('curve_chart2'));
                chart2.draw(data, options);
            }

        }

        function lightGraph(newValue, allLight) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allLight;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.light);
                    oldArray = updatedArray;
                    lightArray = oldArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Light Intensity Review',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart3'));
                chart3.draw(data, options);
            }

        }



        //curve_chart_current_temperature
        function currentTemperatureGraph(newValue, oldTempValue) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = oldTempValue;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.temperature);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Current Temperature',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_current_temperature'));
                chart3.draw(data, options);
            }

        }

        function previousTemperatureGraph(newValue, oldTempValue) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = oldTempValue;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.temperature);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Previous Temperature',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_previous_temperature'));
                chart3.draw(data, options);
            }

        }

        function earlierTemperatureGraph(newValue, oldTempValue) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = oldTempValue;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.temperature);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Earlier Temperature',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_earlier_temperature'));
                chart3.draw(data, options);
            }

        }
        //        //curve_chart_current_noise
        function currentNoiseGraph(newMaxValue, newMinValue, allNoise) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allNoise;
                if (newMaxValue) {
                    var updatedArray = changeNoiseArrayQueue(oldArray, newMaxValue, newMinValue);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);
                var options = {
                    vAxis: {
                        maxValue: 2,
                        minValue: -1
                        //gridlines:{interval:1}
                    },
                    hAxis: { gridlines: { interval: 3 } },
                    title: 'Current Noise',
                    curveType: 'none',
                    legend: { position: 'bottom' }
                };

                var chart = new google.visualization.LineChart(document.getElementById('curve_chart_current_noise'));
                chart.draw(data, options);
            }

        }

        function previousNoiseGraph(newMaxValue, newMinValue, allNoise) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allNoise;
                if (newMaxValue) {
                    var updatedArray = changeNoiseArrayQueue(oldArray, newMaxValue, newMinValue);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);
                var options = {
                    vAxis: {
                        maxValue: 2,
                        minValue: -1
                        //gridlines:{interval:1}
                    },
                    hAxis: { gridlines: { interval: 3 } },
                    title: 'Previous Noise',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart = new google.visualization.LineChart(document.getElementById('curve_chart_previous_noise'));
                chart.draw(data, options);
            }

        }

        function earlierNoiseGraph(newMaxValue, newMinValue, allNoise) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allNoise;
                if (newMaxValue) {
                    var updatedArray = changeNoiseArrayQueue(oldArray, newMaxValue, newMinValue);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);
                var options = {
                    vAxis: {
                        maxValue: 2,
                        minValue: -1
                        //gridlines:{interval:1}
                    },
                    hAxis: { gridlines: { interval: 3 } },
                    title: 'Earlier Noise',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart = new google.visualization.LineChart(document.getElementById('curve_chart_earlier_noise'));
                chart.draw(data, options);
            }

        }
        //curve_chart_current_humidity
        function currentHumidityGraph(newValue, allHum) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allHum;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.humidity);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Current Humidity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_current_humidity'));
                chart3.draw(data, options);
            }

        }

        function previousHumidityGraph(newValue, allHum) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allHum;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.humidity);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Previous Humidity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_previous_humidity'));
                chart3.draw(data, options);
            }

        }

        function earlierHumidityGraph(newValue, allHum) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allHum;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.humidity);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Earlier Humidity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_earlier_humidity'));
                chart3.draw(data, options);
            }

        }

        //        //curve_chart_current_light
        function currentLightGraph(newValue, allLight) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allLight;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.light);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Current Light Intensity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_current_light'));
                chart3.draw(data, options);
            }

        }

        function previousLightGraph(newValue, allLight) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allLight;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.light);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Previous Light Intensity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_previous_light'));
                chart3.draw(data, options);
            }

        }

        function earlierLightGraph(newValue, allLight) {
            google.charts.load('current', { 'packages': ['corechart'] });
            google.charts.setOnLoadCallback(drawChart);

            function drawChart() {
                var oldArray = allLight;
                if (newValue) {
                    var updatedArray = changeArrayQueue(oldArray, newValue.light);
                    oldArray = updatedArray;
                }
                var data = google.visualization.arrayToDataTable(oldArray);


                var options = {
                    vAxis: {
                        maxValue: 5,
                        minValue: 0
                    },
                    title: 'Earlier Light Intensity',
                    curveType: 'function',
                    legend: { position: 'bottom' }
                };

                var chart3 = new google.visualization.LineChart(document.getElementById('curve_chart_earlier_light'));
                chart3.draw(data, options);
            }

        }


        function changeArrayQueue(itemArray, newItem) {
            var i = 0;
            var changedArray = [];
            itemArray.forEach(function (item) {
                if (i != 1) {
                    changedArray.push(item);
                }
                i = i + 1;
            });
            var newItemAsFloat = parseFloat(newItem);
            changedArray.push([vm.timeToGraph, newItemAsFloat]);
            return changedArray;
        }

        function changeNoiseArrayQueue(itemArray, newMaxValue, newMinValue) {
            var i = 0;
            var changedArray = [];
            itemArray.forEach(function (item) {
                if (i != 1) {
                    changedArray.push(item);
                }
                i = i + 1;
            });
            var newMaxNoiseItemAsFloat = parseFloat(newMaxValue);
            var newMinNoiseItemAsFloat = parseFloat(newMinValue);
            changedArray.push([vm.timeToGraph, newMaxNoiseItemAsFloat, newMinNoiseItemAsFloat]);
            return changedArray;
        }
        function mokValueUpdate() {
            var sensorValue = {};
            sensorValue.temperature = Math.random() * 25;
            sensorValue.humidity = Math.random() * 22;
            sensorValue.light = Math.random() * 29;
            return sensorValue;
        }
        function sleep(milliseconds) {
            const date = Date.now();
            let currentDate = null;
            do {
                currentDate = Date.now();
            } while (currentDate - date < milliseconds);
        }
        vm.activated = true;
        vm.determinateValue = 30;

        // Iterate every 100ms, non-stop and increment
        // the Determinate loader.
        $interval(function () {

            vm.determinateValue += 1;
            if (vm.determinateValue > 100) {
                vm.determinateValue = 30;
            }

        }, 100);

        Sockconnect();

        function httpGet(theUrl) {
            var xmlHttp = new XMLHttpRequest();
            xmlHttp.open("GET", theUrl, false); // false for synchronous request
            xmlHttp.send(null);
            return xmlHttp.responseText;
        }
        vm.lampProgress = false;

        vm.lampOnOff = lampOnOff;
        function lampOnOff() {
            vm.lampProgress = true;
            var feedback;
            if ($scope.light == true) {
                feedback = httpGet("http://192.168.2.105:8080/setlampdown");
                //console.log(feedback);
                if (feedback == "Relay for Lamp is set to Off") {
                    vm.lampProgress = false;
                    $scope.light = false;
                }
            }
            else {
                feedback = httpGet("http://192.168.2.105:8080/setlampup");
                //console.log(feedback);
                if (feedback == "Relay for Lamp is set to On") {
                    vm.lampProgress = false;
                    $scope.light = true;
                }

            }

        }
        vm.dehumidifierProgress = false;

        vm.dehumidifierOnOff = dehumidifierOnOff;
        function dehumidifierOnOff() {
            vm.dehumidifierProgress = true;
            var feedback;
            if ($scope.humidifier == true) {

                feedback = httpGet("http://192.168.2.105:8080/setdehumidifierdown");
                //console.log(feedback);
                if (feedback == "Relay for Dehumidifier is set to Off") {
                    vm.dehumidifierProgress = false;
                    $scope.humidifier = false;
                }

            }
            else {

                feedback = httpGet("http://192.168.2.105:8080/setdehumidifierup");
                //console.log(feedback);
                if (feedback == "Relay for Dehumidifier is set to On") {
                    vm.dehumidifierProgress = false;
                    $scope.humidifier = true;
                }

            }
        }


        ////Date Picker Code

        this.myDate = new Date();

        this.minDate = new Date(
            this.myDate.getFullYear(),
            this.myDate.getMonth() - 1,
            this.myDate.getDate() + 2
        );

        this.maxDate = new Date(
            this.myDate.getFullYear(),
            this.myDate.getMonth() + 0,
            this.myDate.getDate()
        );

        /**
         * @param {Date} date
         * @returns {boolean}
         */
        this.onlyWeekendsPredicate = function (date) {
            var day = date.getDay();
            return day === 0 || day === 6;
        };

        /**
         * @param {Date} date
         * @returns {boolean} return false to disable all odd numbered months, true for even months
         */
        this.evenMonthsPredicate = function (date) {
            return date.getMonth() % 2 !== 0;
        };
        vm.hours = ['00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00', '16:00', '17:00', '18:00', '19:00', '20:00', '21:00', '22:00', '23:00'];
        //vm.selectedHour = 'Select Hour';

        vm.clearHour = clearHour;
        function clearHour() {
            vm.selectedHour = '';
        }

        vm.getDayHourSensorData = getDayHourSensorData;
        function getDayHourSensorData(tabNameToShowData) {
            debugger
            var timeToGetData = '' + vm.myDate.getFullYear();
            var month = (vm.myDate.getMonth() + 1);
            if (month < 10) {
                month = '0' + month;
            }
            var date = vm.myDate.getDate()
            if (date < 10) {
                date = '0' + date;
            }

            if (tabNameToShowData == 'temperature') {
                vm.selectedHour = vm.selectedHourTemperature;
            }
            if (tabNameToShowData == 'noise') {
                vm.selectedHour = vm.selectedHourNoise;
            }
            if (tabNameToShowData == 'humidity') {
                vm.selectedHour = vm.selectedHourHumidity;
            }
            if (tabNameToShowData == 'light') {
                vm.selectedHour = vm.selectedHourLight;
            }

            timeToGetData = timeToGetData + month + date + vm.selectedHour[0] + vm.selectedHour[1];
            /////send Request to Get Data
            var dateHourSpecificSensorData = httpGet("http://192.168.2.105:8080/specific_date_hour_sensorinfo?dateHour=" + timeToGetData);
            var fetchedData = [];
            var dbFormatDateHourSpecificSensorData = JSON.parse(dateHourSpecificSensorData);
            dbFormatDateHourSpecificSensorData.forEach(function (envStatus) {
                var env = JSON.parse(envStatus.sensorInfo)
                env.time = envStatus.time;
                fetchedData.push(env);
            });
            fetchedData.reverse();
            if (tabNameToShowData == 'temperature') {
                vm.temperatureReadingsForDateHour = fetchedData;
            }
            if (tabNameToShowData == 'noise') {
                vm.noiseReadingsForDateHour = fetchedData;
            }
            if (tabNameToShowData == 'humidity') {
                vm.humidityReadingsForDateHour = fetchedData;
            }
            if (tabNameToShowData == 'light') {
                vm.lightReadingsForDateHour = fetchedData;
            }
            //clearHour();
        }
        //getDateHourSpecificSensorData();
        function getDateHourSpecificSensorData() {
            var today = new Date();
            var date = today.getFullYear() + '-' + (today.getMonth() + 1) + '-' + today.getDate();
            var time = today.getHours() + ":" + today.getMinutes() + ":" + today.getSeconds();
            var dateTime = date + ' ' + time;
            var timeToGetData = '' + today.getFullYear();
            var month = (today.getMonth() + 1);
            if (month < 10) {
                month = '0' + month;
            }
            var hour = (today.getHours());
            if (hour < 10) {
                hour = '0' + hour;
            }
            var date = today.getDate()
            if (date < 10) {
                date = '0' + date;
            }
            timeToGetData = timeToGetData + month + date + hour;
            var dateHourSpecificSensorData = httpGet("http://192.168.2.105:8080/specific_date_hour_sensorinfo?dateHour=" + timeToGetData);
            var fetchedData = [];
            var dbFormatDateHourSpecificSensorData = JSON.parse(dateHourSpecificSensorData);
            dbFormatDateHourSpecificSensorData.forEach(function (envStatus) {
                var env = JSON.parse(envStatus.sensorInfo)
                env.time = envStatus.time;
                fetchedData.push(env);
            });
            fetchedData.reverse();
            vm.allSensorReadingsForDateHour = fetchedData;
            ///then
            vm.temperatureReadingsForDateHour = vm.allSensorReadingsForDateHour;
            vm.noiseReadingsForDateHour = vm.allSensorReadingsForDateHour;
            vm.humidityReadingsForDateHour = vm.allSensorReadingsForDateHour;
            vm.lightReadingsForDateHour = vm.allSensorReadingsForDateHour;

        }

    }
})();