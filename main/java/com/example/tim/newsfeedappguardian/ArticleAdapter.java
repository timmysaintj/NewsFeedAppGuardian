package com.example.tim.newsfeedappguardian;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_items, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView title = listItemView.findViewById(R.id.title_tv);
        title.setText(currentArticle.getTitle());

        TextView date = listItemView.findViewById(R.id.date_tv);
        date.setText(currentArticle.getDate());


        TextView author = listItemView.findViewById(R.id.author_tv);
        if (currentArticle.getAuthor().equals("")) {
            author.setText(R.string.no_author);
        } else {
            author.setText(currentArticle.getAuthor());
        }

        TextView sectionName = listItemView.findViewById(R.id.category_tv);
        sectionName.setText(currentArticle.getSectionName());

        ImageView thumbnail = listItemView.findViewById(R.id.thumbnail_iv);
        thumbnail.setImageDrawable(currentArticle.getImgThumbnail());

        return listItemView;
    }


}
