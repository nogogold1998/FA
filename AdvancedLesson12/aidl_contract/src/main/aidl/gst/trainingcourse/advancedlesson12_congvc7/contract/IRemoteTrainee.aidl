// IRemoteTrainee.aidl
package gst.trainingcourse.advancedlesson12_congvc7.contract;

import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee;
import gst.trainingcourse.advancedlesson12_congvc7.contract.IRemoteTraineeCallback;

interface IRemoteTrainee {
    long[] add(in Trainee[] trainee);

    int update(in Trainee[] trainee);

    @nullable Trainee findName(in String name);

    boolean registerBestTrainee(in IRemoteTraineeCallback callback);
    boolean unregisterBestTrainee(in IRemoteTraineeCallback callback);
}
