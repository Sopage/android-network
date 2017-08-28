package android.network.binder.local;

interface ILocalCallback {

   void onMessage(int sender, int type, String text);

}
