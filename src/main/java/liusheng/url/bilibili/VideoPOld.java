package liusheng.url.bilibili;

import java.util.List;

/**
 * 年: 2020  月: 03 日: 30 小时: 20 分钟: 28
 * 用户名: LiuSheng
 * https://api.bilibili.com/x/player/playurl?avid=55246614&cid=96602278&bvid=BV1F441147ta&qn=0&type=&otype=json&fnver=0&fnval=16&session=a62a1fcd5a31cc6ccd52b778bfa5ca6a
 */

public class VideoPOld {


    /**
     * code : 0
     * message : 0
     * ttl : 1
     * data : {"from":"local","result":"suee","message":"","quality":80,"format":"flv","timelength":539745,"accept_format":"flv,flv720,flv480,mp4","accept_description":["高清 1080P","高清 720P","清晰 480P","流畅 360P"],"accept_quality":[80,64,32,16],"video_codecid":7,"seek_param":"start","seek_type":"offset","durl":[{"order":1,"length":539745,"size":22746965,"ahead":"","vhead":"","url":"http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=80000000","backup_url":["http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=40000000"]}]}
     */

    private int code;
    private String message;
    private int ttl;
    private DataBean data;
    private String page;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * from : local
         * result : suee
         * message :
         * quality : 80
         * format : flv
         * timelength : 539745
         * accept_format : flv,flv720,flv480,mp4
         * accept_description : ["高清 1080P","高清 720P","清晰 480P","流畅 360P"]
         * accept_quality : [80,64,32,16]
         * video_codecid : 7
         * seek_param : start
         * seek_type : offset
         * durl : [{"order":1,"length":539745,"size":22746965,"ahead":"","vhead":"","url":"http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=80000000","backup_url":["http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=40000000"]}]
         */

        private String from;
        private String result;
        private String message;
        private int quality;
        private String format;
        private int timelength;
        private String accept_format;
        private int video_codecid;
        private String seek_param;
        private String seek_type;
        private List<String> accept_description;
        private List<Integer> accept_quality;
        private List<DurlBean> durl;

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getQuality() {
            return quality;
        }

        public void setQuality(int quality) {
            this.quality = quality;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public int getTimelength() {
            return timelength;
        }

        public void setTimelength(int timelength) {
            this.timelength = timelength;
        }

        public String getAccept_format() {
            return accept_format;
        }

        public void setAccept_format(String accept_format) {
            this.accept_format = accept_format;
        }

        public int getVideo_codecid() {
            return video_codecid;
        }

        public void setVideo_codecid(int video_codecid) {
            this.video_codecid = video_codecid;
        }

        public String getSeek_param() {
            return seek_param;
        }

        public void setSeek_param(String seek_param) {
            this.seek_param = seek_param;
        }

        public String getSeek_type() {
            return seek_type;
        }

        public void setSeek_type(String seek_type) {
            this.seek_type = seek_type;
        }

        public List<String> getAccept_description() {
            return accept_description;
        }

        public void setAccept_description(List<String> accept_description) {
            this.accept_description = accept_description;
        }

        public List<Integer> getAccept_quality() {
            return accept_quality;
        }

        public void setAccept_quality(List<Integer> accept_quality) {
            this.accept_quality = accept_quality;
        }

        public List<DurlBean> getDurl() {
            return durl;
        }

        public void setDurl(List<DurlBean> durl) {
            this.durl = durl;
        }

        public static class DurlBean {
            /**
             * order : 1
             * length : 539745
             * size : 22746965
             * ahead :
             * vhead :
             * url : http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=80000000
             * backup_url : ["http://upos-sz-mirrorks3.bilivideo.com/upgcxcode/60/13/170181360/170181360-1-80.flv?e=ig8euxZM2rNcNbRV7WdVhoM1hwUVhwdEto8g5X10ugNcXBlqNxHxNEVE5XREto8KqJZHUa6m5J0SqE85tZvEuENvNo8g2ENvNo8i8o859r1qXg8xNEVE5XREto8GuFGv2U7SuxI72X6fTr859r1qXg8gNEVE5XREto8z5JZC2X2gkX5L5F1eTX1jkXlsTXHeux_f2o859IB_&uipk=5&nbs=1&deadline=1586058565&gen=playurl&os=ks3bv&oi=2090872692&trid=d9f5bc3883c24580a391cbfbd2d0e889u&platform=pc&upsig=9edfc8fd60d1a0b6e90aa3d09fb4b23b&uparams=e,uipk,nbs,deadline,gen,os,oi,trid,platform&mid=37044417&logo=40000000"]
             */

            private int order;
            private int length;
            private int size;
            private String ahead;
            private String vhead;
            private String url;
            private List<String> backup_url;

            public int getOrder() {
                return order;
            }

            public void setOrder(int order) {
                this.order = order;
            }

            public int getLength() {
                return length;
            }

            public void setLength(int length) {
                this.length = length;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getAhead() {
                return ahead;
            }

            public void setAhead(String ahead) {
                this.ahead = ahead;
            }

            public String getVhead() {
                return vhead;
            }

            public void setVhead(String vhead) {
                this.vhead = vhead;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public List<String> getBackup_url() {
                return backup_url;
            }

            public void setBackup_url(List<String> backup_url) {
                this.backup_url = backup_url;
            }
        }
    }
}
