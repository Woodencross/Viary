package com.hyxy.viary;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ListView diaryListView;
    private List<DiaryItem> monthDiaryList=new ArrayList<>();
    Dbo db_helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        //api高于23时，动态申请权限
        verifyStoragePermissions(MainActivity.this);
        //打开数据库
        db_helper = new Dbo(MainActivity.this);
        //用下面这个读取数据库，返回到cursor
        SQLiteDatabase db = db_helper.getReadableDatabase();
        Cursor res;
        monthDiaryList.add(new DiaryItem(2020,6,16,"June sixteen"));
        monthDiaryList.add(new DiaryItem(2020,6,17,null));
        monthDiaryList.add(new DiaryItem(2020,6,18,"June eighteen"));
        monthDiaryList.add(new DiaryItem(2020,6,19,null));
        monthDiaryList.add(new DiaryItem(2020,6,20,null));
        monthDiaryList.add(new DiaryItem(2020,6,21,"豫章故郡，洪都新府。星分翼轸，地接衡庐。襟三江而带五湖，控蛮荆而引瓯越。物华天宝，龙光射牛斗之墟；人杰地灵，徐孺下陈蕃之榻。雄州雾列，俊采星驰。台隍枕夷夏之交，宾主尽东南之美。都督阎公之雅望，棨戟遥临；宇文新州之懿范，襜帷暂驻。十旬休假，胜友如云；千里逢迎，高朋满座。腾蛟起凤，孟学士之词宗；紫电青霜，王将军之武库。家君作宰，路出名区；童子何知，躬逢胜饯。\n" +
                "时维九月，序属三秋。潦水尽而寒潭清，烟光凝而暮山紫。俨骖騑于上路，访风景于崇阿；临帝子之长洲，得天人之旧馆。层峦耸翠，上出重霄；飞阁流丹，下临无地。鹤汀凫渚，穷岛屿之萦回；桂殿兰宫，即冈峦之体势。\n" +
                "披绣闼，俯雕甍，山原旷其盈视，川泽纡其骇瞩。闾阎扑地，钟鸣鼎食之家；舸舰弥津，青雀黄龙之舳。云销雨霁，彩彻区明。落霞与孤鹜齐飞，秋水共长天一色。渔舟唱晚，响穷彭蠡之滨；雁阵惊寒，声断衡阳之浦。\n" +
                "遥襟甫畅，逸兴遄飞。爽籁发而清风生，纤歌凝而白云遏。睢园绿竹，气凌彭泽之樽；邺水朱华，光照临川之笔。四美具，二难并。穷睇眄于中天，极娱游于暇日。天高地迥，觉宇宙之无穷；兴尽悲来，识盈虚之有数。望长安于日下，目吴会于云间。地势极而南溟深，天柱高而北辰远。关山难越，谁悲失路之人？萍水相逢，尽是他乡之客。怀帝阍而不见，奉宣室以何年？\n" +
                "嗟乎！时运不齐，命途多舛。冯唐易老，李广难封。屈贾谊于长沙，非无圣主；窜梁鸿于海曲，岂乏明时？所赖君子见机，达人知命。老当益壮，宁移白首之心？穷且益坚，不坠青云之志。酌贪泉而觉爽，处涸辙以犹欢。北海虽赊，扶摇可接；东隅已逝，桑榆非晚。孟尝高洁，空余报国之情；阮籍猖狂，岂效穷途之哭！\n" +
                "勃，三尺微命，一介书生。无路请缨，等终军之弱冠；有怀投笔，慕宗悫之长风。舍簪笏于百龄，奉晨昏于万里。非谢家之宝树，接孟氏之芳邻。他日趋庭，叨陪鲤对；今兹捧袂，喜托龙门。杨意不逢，抚凌云而自惜；钟期既遇，奏流水以何惭？\n" +
                "呜乎！胜地不常，盛筵难再；兰亭已矣，梓泽丘墟。临别赠言，幸承恩于伟饯；登高作赋，是所望于群公。敢竭鄙怀，恭疏短引；一言均赋，四韵俱成。请洒潘江，各倾陆海云尔"));
        DiaryAdapter adapter=new DiaryAdapter(MainActivity.this,monthDiaryList);
        adapter.notifyDataSetChanged();
        diaryListView=(ListView)findViewById(R.id.diary_listview);
        diaryListView.setAdapter(adapter);
        diaryListView.setOnItemClickListener(DiaryItemClickListener);

        //安卓21以上时设置状态栏颜色和皮肤风格统一，使界面更加美观
        params.windowColor(MainActivity.this);
    }

    private AdapterView.OnItemClickListener DiaryItemClickListener=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            DiaryItem d=monthDiaryList.get(position);
            Intent intent=new Intent(MainActivity.this,ChooseActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt(params.YearKey,d.getYear());
            bundle.putInt(params.MonthKey,d.getMonth());
            bundle.putInt(params.DayKey,d.getDay());
            bundle.putBoolean("new", false);
            //！！这里传个标题就行，不要内容，显示的时候也显示标题即可
            //bundle.putString(params.TitleKey, d.getDiaryContent());
            //不过可以做测试用
            bundle.putString("content",d.getDiaryContent());
            intent.putExtras(bundle);
            startActivityForResult(intent,1);
        }
    };

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:

            default:
        }
        startActivity(new Intent(MainActivity.this, ChooseActivity.class));
    }

    //读写权限列表
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    //申请读写权限
    public void verifyStoragePermissions(Activity activity){
        //低于6.0，无需动态申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检测是否有读的权限
            int permission = activity.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE");
            // 没有读的权限，去申请读写的权限，会弹出对话框
            if (permission != PackageManager.PERMISSION_GRANTED)
                activity.requestPermissions(PERMISSIONS_STORAGE, 1);
        }
    }
}
