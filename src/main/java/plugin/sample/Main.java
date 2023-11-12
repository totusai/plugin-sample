package plugin.sample;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import javax.crypto.Mac;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.World;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    //intの初期値は0なので、わざわざ0で初期化する必要はない
    private Integer count = 0;
    private World world;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

    }

    /**
     * プレイヤーがスニークを開始/終了する際に起動されるイベントハンドラ。
     *
     * @param e イベント
     */
    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent e) throws IOException {
        // イベント発生時のプレイヤーやワールドなどの情報を変数に持つ。

        //player情報を取得する
        Player player = e.getPlayer();
        //ワールド情報を取得する
        World korld = player.getWorld();

        BigInteger val = new BigInteger(count.toString());

        List<Color> colorList = List.of(Color.RED, Color.BLUE, Color.WHITE, Color.BLACK);

        if (val.isProbablePrime(1)) {

            for (Color color : colorList) {

                // 花火オブジェクトをプレイヤーのロケーション地点に対して出現させる。
                Firework firework = world.spawn(player.getLocation(), Firework.class);

                // 花火オブジェクトが持つメタ情報を取得。
                FireworkMeta fireworkMeta = firework.getFireworkMeta();

                // メタ情報に対して設定を追加したり、値の上書きを行う。
                // 今回は青色で星型の花火を打ち上げる。
                fireworkMeta.addEffect(
                    FireworkEffect.builder()
                        .withColor(color)
                        .with(Type.BALL_LARGE)
                        .withFlicker()
                        .build());
                fireworkMeta.setPower(1);

                // 追加した情報で再設定する。
                firework.setFireworkMeta(fireworkMeta);

                System.out.println(val + "は素数です");
            }
            Path path = Path.of("firework.txt");
            Files.writeString(path, "たーまやー", StandardOpenOption.APPEND);
            player.sendMessage(Files.readString(path));
        }
        count++;
    }
    
    //palerがベッドに入ろうとした時のイベント
    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent e) {
        //Playerの持っているインベントリからゲットコンテンツする
        Player player = e.getPlayer();
        //インベントリは自分が所持しているアイテム
        //ItemStackとは自分が持っている全てのアイテムを取得するという意味
        ItemStack[] itemStacks = player.getInventory().getContents();
        //もし、アイテムという要素がNullではない（!Objects.isNull(item)）!をつけることで反転している
        //かつ、アイテムがMaxStackSize(どれだけ入れられるか(マインクラフト上では64と決まっている))が64
        //かつ、item.getAmount（現在のアイテム所持数）が64未満なら
        //items.setAmount(アイテム所持数)を64にする
        //つまり、ベッドに入ったら所持しているアイテムが64個に増えるという内容
        for (ItemStack item : itemStacks) {
            if (!Objects.isNull(item) && item.getMaxStackSize() == 64 && item.getAmount() <  64) {
                item.setAmount(64);
            }
        }
        //for文で取得した情報をプレイヤーに反映させる
        player.getInventory().setContents(itemStacks);
    }
}
