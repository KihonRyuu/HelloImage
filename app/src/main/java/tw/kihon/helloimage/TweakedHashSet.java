package tw.kihon.helloimage;

import java.util.LinkedHashSet;

class TweakedHashSet<T> extends LinkedHashSet<T> {

    @Override
    public boolean add(T e) {
        boolean wasThere = remove(e);
        super.add(e);
        return !wasThere;
    }

}