package cn.edu.scau.hometown.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Parcel;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.scau.hometown.R;
import cn.edu.scau.hometown.bean.HmtForumPostList;
import cn.edu.scau.hometown.tools.DataUtil;
import cn.edu.scau.hometown.tools.HttpUtil;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2015/8/31 0031.
 * 用于渲染论坛帖子列表，填充帖子列表视图的Adapter类
 */
public class InitHmtForumListViewAdapter extends RecyclerView.Adapter<InitHmtForumListViewAdapter.ViewHolder> {
    private HmtForumPostList hmtForumPostList;
    private DisplayImageOptions options;
    private RequestQueue requestQueue;
    private Context context;


    public InitHmtForumListViewAdapter(HmtForumPostList hmtForumPostList, Context contex) {
        this.hmtForumPostList = hmtForumPostList;
        this.context = contex;
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.ARGB_4444)
                .build();
        requestQueue = Volley.newRequestQueue(contex);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.listview_item_hmt_forum_, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HmtForumPostList.ThreadsEntity threadlistEntity = hmtForumPostList.getThreads().get(position);
        String dateline = threadlistEntity.getDateline();
        String authorId = threadlistEntity.getAuthorid();
        String subject = threadlistEntity.getSubject();
        String replies = threadlistEntity.getReplies();
        String author = threadlistEntity.getAuthor();


        String message = threadlistEntity.getMessage();
        message = message.replaceFirst("\\[i=s\\]", "【");
        message = message.replaceFirst("\\[/i\\]", "】");
        message = message.replaceAll("\\[.*?\\]", "");
        message = message.replaceAll("\\s*\n", "");


        holder.tv_title_of_forum_threads.setText(subject);
        holder.tv_replies_of_forum_threads.setText(replies);
        holder.tv_author_of_forum_threads.setText(author);
        holder.tv_content_of_forum_threads.setText(EditModified(new SpannableString("\t\t\t" + message)));
        holder.tv_lastpost_of_forum_threads.setText("发表于 " + DataUtil.times(dateline));

        // ImageLoader.getInstance().displayImage(HttpUtil.GET_USER_ICON_BY_USER_ID + authorId,holder.civ_icon_of_forum_threads, options);
        if (!author.equals("匿名"))
            HttpUtil.setUserIconTask(requestQueue, HttpUtil.GET_USER_ICON_BY_USER_ID + authorId, holder.civ_icon_of_forum_threads);
        else
            holder.civ_icon_of_forum_threads.setImageDrawable(context.getResources().getDrawable(R.drawable.default_user_image));
    }

    @Override
    public int getItemCount() {
        return Integer.valueOf(hmtForumPostList.getThreads().size());
    }

    /**
     * 对【本帖最后由[\s\S]+编辑】这种内容进行标记
     *
     * @param spannableString 帖子的内容
     * @return
     */
    private SpannableString EditModified(SpannableString spannableString) {

        String regexQuote = "【 本帖最后由[\\s\\S]+编辑 】";
        Pattern patternQuote = Pattern.compile(regexQuote);
        Matcher matcherQuote = patternQuote.matcher(spannableString);
        while (matcherQuote.find()) {
            int start = matcherQuote.start();
            int end = matcherQuote.end();
            ForegroundColorSpan foreColorSpan = new ForegroundColorSpan(0xbb9575cd);
            Parcel p = Parcel.obtain();
            p.writeInt(Typeface.BOLD_ITALIC);
            p.setDataPosition(0);
            StyleSpan ss = new StyleSpan(p);
            spannableString.setSpan(foreColorSpan, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            spannableString.setSpan(ss, start, end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        }
        return spannableString;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_title_of_forum_threads;
        public TextView tv_content_of_forum_threads;
        public TextView tv_replies_of_forum_threads;
        public TextView tv_author_of_forum_threads;
        public TextView tv_lastpost_of_forum_threads;
        public CircleImageView civ_icon_of_forum_threads;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_title_of_forum_threads = (TextView) itemView.findViewById(R.id.tv_titile_of_forum_threads);
            tv_content_of_forum_threads = (TextView) itemView.findViewById(R.id.tv_content_of_forum_threads);
            tv_replies_of_forum_threads = (TextView) itemView.findViewById(R.id.tv_replies_of_forum_threads);
            tv_author_of_forum_threads = (TextView) itemView.findViewById(R.id.tv_author_of_forum_threads);
            tv_lastpost_of_forum_threads = (TextView) itemView.findViewById(R.id.tv_lastpost_of_forum_threads);
            civ_icon_of_forum_threads = (CircleImageView) itemView.findViewById(R.id.tv_icon_of_forum_threads);
        }
    }
    public void clean(){
        requestQueue.stop();
        requestQueue.cancelAll(true);

    }

}