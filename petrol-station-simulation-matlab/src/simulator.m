function output = simulator(n, p)
% n = number of vehicles
% p = 0 (non-peak hour), 1 (peak hour)

%if p ~= 0 && p ~= 1
%    error('Invalid input: p must be 0 or 1');
%end

% Initialize pump availability times (4 pumps)
pumps = [0,0,0,0];

% Petrol types and their prices
petrolTypes = {'Primax95', 'Primax97', 'Dynamic Diesel'};
prices = [2.05, 2.50, 2.15];

lastArrival = 0;         % Track last vehicle arrival time
totalWait = 0;           % Sum of all waiting times
waited = 0;              % Number of vehicles that waited

% Data holders
pumpRecord = zeros(n, 1);             % Which pump each vehicle used
refuelTimeArr = zeros(n, 1);          % Refueling duration
startTimeArr = zeros(n, 1);           % Refueling start time
endTimeArr = zeros(n, 1);             % Refueling end time
waitTimeArr = zeros(n, 1);            % Waiting time before refueling
spentTimeArr = zeros(n, 1);           % Total time in system
petrolUsed = cell(n,1);               % Petrol type used
vehicleType = cell(n,1);              % Vehicle type (Motorbike/Car/Lorry)
qtyUsed = zeros(n,1);                 % Fuel quantity (litres)
totalPriceUsed = zeros(n,1);          % Total cost
rInterUsed = zeros(n,1);              % Random number for inter-arrival
interUsed = zeros(n,1);               % Inter-arrival time
arrUsed = zeros(n,1);                 % Arrival time
laneUsed = zeros(n,1);                % Lane used
rRefuelUsed = zeros(n,1);             % Random number for refueling

% Refueling log
eventCount = 0;
eventTime = zeros(2*n, 1);
eventMsg = cell(2*n, 1);

% Print Inter-arrival Time Table
fprintf('\nInter-arrival Time Table \n');
fprintf(' ----------------------------------------------------------------------\n');
fprintf('| Inter-arrival time | Probability |     CDF     | Random number range |\n');
fprintf('|--------------------|-------------|-------------|---------------------|\n');
if p == 1
    fprintf('| 1 min              | 0.50        | 0.50        | 0.00 ~ 0.50          |\n');
    fprintf('| 2 min              | 0.50        | 1.00        | 0.50 ~ 1.00          |\n');
else
    fprintf('| 3 min              | 0.25        | 0.25        | 0.00 ~ 0.25          |\n');
    fprintf('| 4 min              | 0.25        | 0.50        | 0.25 ~ 0.50          |\n');
    fprintf('| 5 min              | 0.25        | 0.75        | 0.50 ~ 0.75          |\n');
    fprintf('| 6 min              | 0.25        | 1.00        | 0.75 ~ 1.00          |\n');
end
fprintf(' -----------------------------------------------------------------------\n');

% Print Type of Petrol Table
fprintf('\nType of Petrol Table\n');
fprintf(' ------------------------------------------------------------------------------------\n');
fprintf('| Type of petrol     | Probability |  CDF  | Random number range  | Price/Litre (RM) |\n');
fprintf('|--------------------|-------------|-------|----------------------|------------------|\n');
fprintf('| Primax95           | 0.45        | 0.45  | 0.00 ~ 0.45          | 2.05             |\n');
fprintf('| Primax97           | 0.45        | 0.90  | 0.45 ~ 0.90          | 2.50             |\n');
fprintf('| Dynamic Diesel     | 0.10        | 1.00  | 0.90 ~ 1.00          | 2.15             |\n');
fprintf(' ------------------------------------------------------------------------------------\n');

% Print Refueling Time Table
fprintf('\nRefueling Time Table \n');
fprintf(' --------------------------------------------------------------------------------\n');
fprintf('| Vehicle    | Refueling Time Range | Probability |  CDF   | Random number range |\n');
fprintf('|------------|----------------------|-------------|--------|---------------------|\n');
fprintf('| Motorbike  | 2 ~ 4 min            | 0.30        | 0.30   | 0.00 ~ 0.30         |\n');
fprintf('| Car        | 4 ~ 8 min            | 0.60        | 0.90   | 0.30 ~ 0.90         |\n');
fprintf('| Lorry      | 6 ~ 10 min           | 0.10        | 1.00   | 0.90 ~ 1.00         |\n');
fprintf(' --------------------------------------------------------------------------------\n');

% --- Start Simulation Loop ---
for i = 1:n
    rInter = rand;  % Random number for inter-arrival time
    if p == 1
        interArrival = floor(rand * 2) + 1;
    else
        interArrival = floor(rand * 4) + 3;
    end
    arrivalTime = lastArrival + interArrival;

    rRefuel = rand;  % Random number for refueling (vehicle type)
    if rRefuel < 0.3
        vType = 'Motorbike'; fuelOptions = [1,2];
        qty = floor(rand * 4) + 3; refuelTime = floor(rand * 3) + 2;
    elseif rRefuel < 0.9
        vType = 'Car'; fuelOptions = [1,2];
        qty = floor(rand * 31) + 20; refuelTime = floor(rand * 5) + 4;
    else
        vType = 'Lorry'; fuelOptions = [3];
        qty = floor(rand * 41) + 60; refuelTime = floor(rand * 5) + 6;
    end

    petrolIndex = fuelOptions(floor(rand * length(fuelOptions)) + 1);
    petrol = petrolTypes{petrolIndex};
    pricePerLitre = prices(petrolIndex);
    totalPrice = qty * pricePerLitre;

    % Choose lane and pump
    lane1Ready = min(max(arrivalTime,pumps(1)), max(arrivalTime,pumps(2)));
    lane2Ready = min(max(arrivalTime,pumps(3)), max(arrivalTime,pumps(4)));
    if lane1Ready <= lane2Ready
        laneNum = 1; pumpChoice = [1,2];
    else
        laneNum = 2; pumpChoice = [3,4];
    end

    % Select earliest available pump from that lane
    bestPump = pumpChoice(1);
    earliestTime = max(arrivalTime, pumps(bestPump));
    for j = 2:length(pumpChoice)
        pnum = pumpChoice(j);
        ready = max(arrivalTime, pumps(pnum));
        if ready < earliestTime
            earliestTime = ready;
            bestPump = pnum;
        end
    end

    % Calculate timings
    startTime = earliestTime;
    endTime   = startTime + refuelTime;
    waitTime  = startTime - arrivalTime;
    timeSpent = endTime - arrivalTime;

    if waitTime > 0
        waited = waited + 1;
        totalWait = totalWait + waitTime;
    end

    % Store data
    pumps(bestPump)   = endTime;
    lastArrival       = arrivalTime;
    pumpRecord(i)     = bestPump;
    refuelTimeArr(i)  = refuelTime;
    startTimeArr(i)   = startTime;
    endTimeArr(i)     = endTime;
    waitTimeArr(i)    = waitTime;
    spentTimeArr(i)   = timeSpent;
    petrolUsed{i}     = petrol;
    vehicleType{i}    = vType;
    qtyUsed(i)        = qty;
    totalPriceUsed(i) = totalPrice;
    rInterUsed(i)     = rInter;
    interUsed(i)      = interArrival;
    arrUsed(i)        = arrivalTime;
    laneUsed(i)       = laneNum;
    rRefuelUsed(i)    = rRefuel;

    % Event logs
    eventCount = eventCount + 1;
    eventTime(eventCount) = arrivalTime;
    eventMsg{eventCount} = sprintf('Vehicle %d arrived at minute %d and began refueling with %s at Pump Island %d.', ...
        i, arrivalTime, petrol, bestPump);
    eventCount = eventCount + 1;
    eventTime(eventCount) = endTime;
    eventMsg{eventCount} = sprintf('Vehicle %d finished refueling and departed at minute %d.', i, endTime);
end

% Display sorted event logs
fprintf('\n');
for i = 1:eventCount - 1
    for j = i+1:eventCount
        if eventTime(j) < eventTime(i)
            tmpT = eventTime(i); eventTime(i) = eventTime(j); eventTime(j) = tmpT;
            tmpM = eventMsg{i};  eventMsg{i} = eventMsg{j};  eventMsg{j} = tmpM;
        end
    end
end
for i = 1:eventCount
    fprintf('%s\n', eventMsg{i});
end

% Display Simulation Table Part 1
fprintf('\nSimulation Table \n')
fprintf(' -------------------------------------------------------------------------------------------------------------------------------------\n');
fprintf('| Vehicle |  Type of  |     Type of    | Quantity | Total price | Random number for  | Inter-arrival | Arrival | Line | Random number |\n');
fprintf('| number  |  vehicle  |     petrol     | (liter)  |     (RM)    | inter-arrival time |     time      |  time   |number| for refueling |\n');
fprintf('|---------|-----------|----------------|----------|-------------|--------------------|---------------|---------|------|---------------|\n');
for i = 1:n
    fprintf('| %-7d | %-9s | %-14s | %-8d | %-11.2f | %-18.2f | %-13d | %-7d | %-4d | %-13.2f |\n', ...
        i, vehicleType{i}, petrolUsed{i}, qtyUsed(i), totalPriceUsed(i), rInterUsed(i), interUsed(i), arrUsed(i), laneUsed(i), rRefuelUsed(i));
end
fprintf(' -------------------------------------------------------------------------------------------------------------------------------------\n');

% Display Simulation Table Part 2
fprintf('\n -----------------------------------------------------------------------------------------------------------------------------------------------------\n');
fprintf('|         |           Pump 1          |           Pump 2          |           Pump 3          |           Pump 4          |              |            |\n');
fprintf('| Vehicle |---------------------------|---------------------------|---------------------------|---------------------------| Waiting time | Time spent |\n');
fprintf('| number  | Refueling |  Time  | Time | Refueling |  Time  | Time | Refueling |  Time  | Time | Refueling |  Time  | Time |              |            |\n');
fprintf('|         |   time    | begins | ends |   time    | begins | ends |   time    | begins | ends |   time    | begins | ends |              |            |\n');
fprintf('|---------|-----------|--------|------|-----------|--------|------|-----------|--------|------|-----------|--------|------|--------------|------------|\n');
for i = 1:n
    fprintf('| %-7d ', i);
    for k = 1:4
        if pumpRecord(i) == k
            fprintf('| %-8d  | %-6d | %-4d ', refuelTimeArr(i), startTimeArr(i), endTimeArr(i));
        else
            fprintf('| -         | -      | -    ');
        end
    end
    fprintf('| %-12d | %-10d |\n', waitTimeArr(i), spentTimeArr(i));
end
fprintf(' -----------------------------------------------------------------------------------------------------------------------------------------------------\n');

% Final Simulation Results
avgWait = totalWait / n;
probWait = waited / n;

fprintf('\nSimulation Results \n');
minW = floor(avgWait);
secW = round((avgWait - minW) * 60);
fprintf('Average Waiting Time         : %.2f minutes (%d min %d sec)\n', avgWait, minW, secW);

avgTimeSpent = mean(spentTimeArr);
minS = floor(avgTimeSpent);
secS = round((avgTimeSpent - minS) * 60);
fprintf('Average Time in System       : %.2f minutes (%d min %d sec)\n', avgTimeSpent, minS, secS);

fprintf('Probability of Waiting       : %.2f\n', probWait);

for pumpID = 1:4
    indices = find(pumpRecord == pumpID);
    if ~isempty(indices)
        avgServiceTime = mean(refuelTimeArr(indices));
        minA = floor(avgServiceTime);
        secA = round((avgServiceTime - minA) * 60);
        fprintf('Average Service Time at Pump %d : %.2f minutes (%d min %d sec)\n', ...
            pumpID, avgServiceTime, minA, secA);
    else
        fprintf('Average Service Time at Pump %d : N/A (no vehicle)\n', pumpID);
    end
end

output = [avgWait, probWait];
end
