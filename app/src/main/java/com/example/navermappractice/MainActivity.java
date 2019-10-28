package com.example.navermappractice;

import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;

import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.MarkerIcons;


//OnMapReadyCallback <- Naver 객체 사용 위해 상속(implements)
//implements는 메서드를 재정의 해야함으로 onMapReady를 Override함
public class MainActivity extends FragmentActivity implements OnMapReadyCallback{
    //사용한 프래그먼트
    FragmentManager fm;
    MapFragment mapFragment; //네이버지도가 들어가게 될 객체
    MainButtonFragment mainButtonFragment; //네이버지도에서 동작하는 버튼의 프래그먼트 객체
    MarkerButtonFragment MBF;
    MenuFragment MF;
    WriteFragment WF;




    //사용한 버튼
    Button buttonAddMarker; //누른 위치에 마커를 추가하는 버튼
    Button buttonCreateWrite;
    Button buttonSaveText;
    Button menuButton;

    LinearLayout LL;
    TextView TextUser;

    //마커관련
    Marker marker; //마커객체
    InfoWindow infoWindow; //마커 클릭 시 표시되는 알림창

    //좌표변수
    String lat;
    String lng;

    NaverMap NM;

    public MainActivity(){
        marker = new Marker();
        infoWindow = new InfoWindow();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //xml에 정의된 UI를 뿌려줌

        InitNaverMap(savedInstanceState); //기초 화면이 되는 naverMap(프래그먼트)에 네이버지도를 넣음

        addFragment(mainButtonFragment.newinstance()); //mainButtonFragment를 지도 위에 덮어써줌
        addFragment(MBF.newInstance());
        addFragment(MF.newInstance());


    }
//=============================================================================================
    //네이버지도객체를 초기화시켜줌
    public void InitNaverMap(Bundle savedInstanceState){
        //네이버 지도를 Fragment로 객체화함
        fm  = getSupportFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map_fragment);

        if (savedInstanceState == null){
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map_fragment , mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
    }

    //사용하진 않았지만 프래그먼트를 교체해주는 것 (추가해주는 것과 다름)
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment, fragment);
        fragmentTransaction.commit();
    }
    // 프래그먼트 객체를 추가로 덮어써줌
    private void addFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map_fragment, fragment);
        fragmentTransaction.commit();

    }
    private void removeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commit();
    }
    // 교체 할시 교체 되기전 Fragment를 BackStack에 저장
    private void backStackOfReplaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    // Add 할시 Add 되기전 Fragment를 BackStack에 저장
    private void backStackOfAddFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.map_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    // 제거 할시 제거 되기전 Fragment를 BackStack에 저장
    private void backStackOfRemoveFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

//============================================================================================
    @Override
    //네이버지도 객체가 실행될 때 실행
    public void onMapReady(@NonNull final NaverMap naverMap) {

        SetOnMapClickListener(naverMap);

        SetButtonAddMarkerListener();

        SetButtonMenuListener();

        SetMarkerButtonListener();
    }

    public void SetButtonMenuListener(){

        menuButton =(Button)findViewById(R.id.menu);

        menuButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                PopupMenu popup= new PopupMenu(getApplicationContext(), v);//v는 클릭된 뷰를 의미
                getMenuInflater().inflate(R.menu.marker_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.item1:
                                Toast.makeText(getApplication(),"메뉴1",Toast.LENGTH_SHORT).show();
// ===================================================================================================
//                                Intent intent = new Intent(MainActivity.this,MainActivity.class);
//                                startActivity(intent);
//                                finish();
// ===================================================================================================
                                break;
                            case R.id.item2:
                                Toast.makeText(getApplication(),"메뉴2",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.item3:
                                Toast.makeText(getApplication(),"메뉴3",Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

    }
//=============================================================================================
//지도 클릭 리스너 정의부분
    public void SetOnMapClickListener(@NonNull final NaverMap naverMap){
        NM=naverMap;
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            //지도 클릭 이벤트 리스너 정의
            public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                SetMarkerPosition(latLng);

                marker.setMap(naverMap);


            }
        });
    }

    //buttonAddMarker 버튼 (마커 추가버튼) 리스너 정의
    public void SetButtonAddMarkerListener(){
        buttonAddMarker = (Button)findViewById(R.id.buttonAddMarker);
        buttonAddMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SetMarkerOnClickListener();

                marker.setIcon(MarkerIcons.RED);

                infoWindow = new InfoWindow();
                SetInfoWindow();
                infoWindow.open(marker);

                marker=new Marker(marker.getPosition()); //클릭 시 앞서 받은 포지션에 마커를 추가
            }
        });
    }

    //마커 클릭 리스너 정의  (추가할거 많음) 미완
    public void SetMarkerOnClickListener(){
        marker.setOnClickListener(new Overlay.OnClickListener() {
            @Override
            public boolean onClick(@NonNull Overlay overlay) { // 버튼클릭하여 저장된 마커의 클릭리스너

               // Toast.makeText(getApplicationContext(),"클릭되었습니다!!",Toast.LENGTH_LONG).show();


                LL = (LinearLayout)findViewById(R.id.LinearMarkerButton);


                if(LL.getVisibility()==LL.VISIBLE){
                    LL.setVisibility(LL.INVISIBLE);
                }
                else{
                    LL.setVisibility(LL.VISIBLE);
                    SetWriteButtonListener();
                }

                return true;
            }
        });
    }


    //마커 정보창에 대한 함수(미완성)
    public void SetInfoWindow(){
        infoWindow.setAdapter(new InfoWindow.DefaultTextAdapter(mapFragment.getActivity()){
            @NonNull
            @Override
            public CharSequence getText(@NonNull InfoWindow infoWindow){
                return "정보창";
            }
        });
    }

    //마커클릭시 나오는 버튼 리스너
    public void SetMarkerButtonListener(){

    }

    //좌표를 받아 마커가 찍힐 포지션을 넣어준다.
    public void SetMarkerPosition(@NonNull LatLng latLng){
        //위도
        lat = Double.toString(latLng.latitude);
        double dLat = Double.parseDouble(lat);
        //경도
        lng = Double.toString(latLng.longitude);
        double dLng = Double.parseDouble(lng);

        marker.setPosition(new LatLng(dLat, dLng));
    }
    //글쓰기 버튼 클릭 리스너 정의
    public void SetWriteButtonListener() {
        buttonCreateWrite = (Button) findViewById(R.id.write_button);
        buttonCreateWrite.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                backStackOfAddFragment(WF.newInstance());
                Toast.makeText(getApplicationContext(), "클릭되었습니다!!", Toast.LENGTH_LONG).show();
                //SetSaveTextButtonListener();
                LL.setVisibility(LL.INVISIBLE);
            }
        });
    }
}
