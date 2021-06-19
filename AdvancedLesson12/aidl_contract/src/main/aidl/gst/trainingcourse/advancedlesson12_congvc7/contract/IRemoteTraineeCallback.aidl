// IRemoteTraineeCallback.aidl
package gst.trainingcourse.advancedlesson12_congvc7.contract;

import gst.trainingcourse.advancedlesson12_congvc7.contract.Trainee;

interface IRemoteTraineeCallback {
    void onChanged(in @nullable Trainee value);
}
