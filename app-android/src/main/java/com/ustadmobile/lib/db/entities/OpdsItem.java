package com.ustadmobile.lib.db.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;

import com.ustadmobile.lib.db.entities.OpdsEntryWithRelations;
import com.ustadmobile.lib.db.entities.OpdsLink;
import com.ustadmobile.lib.util.UMUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by mike on 1/13/18.
 */

public class OpdsItem {

    /**
     * An autogenerated primary key id for performance reasons. A unique index is defined on other
     * fields as needed (e.g. url, entry index in feed, etc)
     */
    @PrimaryKey(autoGenerate = true)
    protected int id;

    /**
     * The id element as per the XML from the entry/feed
     */
    @ColumnInfo(name = "itemId")
    protected String itemId;

    @ColumnInfo(name = "title")
    protected String title;

    @ColumnInfo(name = "updated")
    protected String updated;

    @ColumnInfo(name = "summary")
    protected String summary;

    @ColumnInfo(name = "content")
    protected String content;

    @ColumnInfo(name = "contentType")
    protected String contentType;

    protected String publisher;

    protected String language;

    protected String url;


    protected static final String ATTR_REL = "rel";

    protected static final String ATTR_TYPE = "type";

    protected static final String ATTR_HREF = "href";

    protected static final String ATTR_LENGTH = "length";

    protected static final String ATTR_TITLE = "title";

    protected static final String ATTR_HREFLANG = "hreflang";



    protected static final String ATTR_SUMMARY = "summary";

    protected static final String ATTR_PUBLISHER = "publisher";


    protected static final String TAG_ENTRY = "entry";

    protected static final String TAG_CONTENT = "content";

    protected static final String TAG_UPDATED = "updated";

    protected static final String TAG_ID = "id";

    protected static final String TAG_LINK = "link";

    /**
     * Entry content type - text
     */
    public static final String CONTENT_TYPE_TEXT = "text";

    /**
     * Entry content type - html
     */
    public static final String CONTENT_TYPE_XHTML = "xhtml";

    public interface OpdsItemLoadCallback {

        void onDone(com.ustadmobile.lib.db.entities.OpdsItem item);

        void onEntryAdded(com.ustadmobile.lib.db.entities.OpdsEntryWithRelations entry, com.ustadmobile.lib.db.entities.OpdsItem parentFeed, int position);

        void onLinkAdded(com.ustadmobile.lib.db.entities.OpdsLink link, com.ustadmobile.lib.db.entities.OpdsItem parentItem, int position);

        void onError(com.ustadmobile.lib.db.entities.OpdsItem item, Throwable cause);

    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void load(XmlPullParser xpp, OpdsItemLoadCallback callback) throws IOException, XmlPullParserException{
        int evtType;
        String name;
        int i, entryCount = 0, linkCount = 0;

        com.ustadmobile.lib.db.entities.OpdsLink link;
        String linkLength;

        while((evtType = xpp.next()) != XmlPullParser.END_DOCUMENT) {
            if(evtType == XmlPullParser.START_TAG) {
                name = xpp.getName();
                if(name.equals(TAG_ENTRY)) {
                    com.ustadmobile.lib.db.entities.OpdsEntryWithRelations newEntry = new OpdsEntryWithRelations();
                    newEntry.load(xpp, callback);
                    newEntry.setFeedIndex(entryCount);
                    newEntry.setFeedId(this.getId());
                    entryCount++;

                    if(callback != null) {
                        callback.onEntryAdded(newEntry, this, entryCount);
                    }
                }else if(name.equals(ATTR_TITLE) && xpp.next() == XmlPullParser.TEXT) {
                    this.title = xpp.getText();
                }else if(name.equals(TAG_ID) && xpp.next() == XmlPullParser.TEXT) {
                    this.itemId= xpp.getText();
                }else if(name.equals(TAG_LINK)){
                    link = new OpdsLink();
                    link.setHref(xpp.getAttributeValue(null, ATTR_HREF));
                    link.setRel(xpp.getAttributeValue(null, ATTR_REL));
                    link.setMimeType(xpp.getAttributeValue(null, ATTR_TYPE));
                    link.setHreflang(xpp.getAttributeValue(null, ATTR_HREFLANG));
                    linkLength = xpp.getAttributeValue(null, ATTR_LENGTH);
                    if(linkLength != null) {
                        try {
                            link.setLength(Long.parseLong(linkLength));
                        }catch(NumberFormatException n) {
                            n.printStackTrace();
                        }
                    }

                    link.setTitle(xpp.getAttributeValue(null, ATTR_TITLE));
                    if(this instanceof com.ustadmobile.lib.db.entities.OpdsEntry) {
                        link.setEntryId(this.getId());
                    }else {
                        link.setFeedId(this.getId());
                    }
                    link.setLinkIndex(linkCount);

                    if(callback != null)
                        callback.onLinkAdded(link, this, linkCount);

                    linkCount++;

//                    for(i = 0; i < UstadJSOPDSItem.LINK_ATTRS_END; i++) {
//                        linkAttrs[i] = xpp.getAttributeValue(null, ATTR_NAMES[i]);
//                    }
//
//                    if(!linkAttrs[ATTR_REL].equals(LINK_REL_SELF_ABSOLUTE)) {
//                        this.addLink(linkAttrs);
//                    }else {
//                        this.href = linkAttrs[ATTR_HREF];
//                    }

                }else if(name.equals(TAG_UPDATED) && xpp.next() == XmlPullParser.TEXT){
                    this.updated = xpp.getText();
                }else if(name.equals(ATTR_SUMMARY) && xpp.next() == XmlPullParser.TEXT) {
                    this.summary = xpp.getText();
                }else if(name.equals(TAG_CONTENT)) {
                    contentType = xpp.getAttributeValue(null, ATTR_TYPE);
                    if(contentType != null && contentType.equals(CONTENT_TYPE_XHTML)) {
//                        this.content = UMUtil.passXmlThroughToString(xpp, TAG_CONTENT);
                    }else if(xpp.next() == XmlPullParser.TEXT){
                        this.content = xpp.getText();
                    }
                }else if(name.equals("dc:publisher") && xpp.next() == XmlPullParser.TEXT){
                    this.publisher = xpp.getText();
                }else if(name.equals("dcterms:publisher") && xpp.next() == XmlPullParser.TEXT){
                    this.publisher = xpp.getText();
                }else if(name.equals("dc:language") && xpp.next() == XmlPullParser.TEXT) {
                    this.language = xpp.getText();
                }else if(name.equals("author")){
//                    TODO: Implement handling authors
//                    UstadJSOPDSAuthor currentAuthor = new UstadJSOPDSAuthor();
//                    do {
//                        evtType = xpp.next();
//
//                        if(evtType == XmlPullParser.START_TAG) {
//                            if(xpp.getName().equals("name")){
//                                if(xpp.next() == XmlPullParser.TEXT) {
//                                    currentAuthor.name = xpp.getText();
//                                }
//                            }else if (xpp.getName().equals("uri")) {
//                                if(xpp.next() == XmlPullParser.TEXT) {
//                                    currentAuthor.uri = xpp.getText();
//                                }
//                            }
//                        }else if(evtType == XmlPullParser.END_TAG
//                                && xpp.getName().equals("author")){
//                            if (this.authors == null){
//                                this.authors = new Vector();
//                                this.authors.addElement(currentAuthor);
//                            }else{
//                                this.authors.addElement(currentAuthor);
//                            }
//                        }
//                    }while(!(evtType == XmlPullParser.END_TAG && xpp.getName().equals("author")));
                }
            }else if(evtType == XmlPullParser.END_TAG) {
                if(xpp.getName().equals(TAG_ENTRY)) {
                    return;
                }
            }
        }

        if(callback != null)
            callback.onDone(this);

    }

}
