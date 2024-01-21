package com.example.festa.services

import com.example.festa.ui.theme.bookmark.model.BookMarkGetResponse
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostBody
import com.example.festa.guest.viewmodel.bookmarkpost.BookMarkPostResponse
import com.example.festa.guest.viewmodel.bookmarkname.BookMarkNameResponse
import com.example.festa.guest.viewmodel.guestlist.GuestListResponse
import com.example.festa.guest.Uploadexcelapi.UploadexcelResponse
import com.example.festa.guest.viewmodel.guests.GuestBody
import com.example.festa.guest.viewmodel.guests.GuestResponse
import com.example.festa.view.cohost.viewmodel.addcohost.AddCoHostBody
import com.example.festa.view.cohost.viewmodel.cohostlist.CostHostListResponse
import com.example.festa.view.createevents.viewmodel.addvenue.AddVenueBody
import com.example.festa.view.createevents.viewmodel.addvenue.AddVenueResponse
import com.example.festa.view.createevents.viewmodel.createevent.CreateEventReponse
import com.example.festa.view.createevents.viewmodel.getedit.GetEditResponse
import com.example.festa.view.createevents.viewmodel.sendinvite.SendInviteResponse
import com.example.festa.view.events.viewmodel.album.AllAlbumResponse
import com.example.festa.view.events.viewmodel.createNewAlbum.CreateAlbumBody
import com.example.festa.view.events.viewmodel.createNewAlbum.CreateNewAlbumResponse
import com.example.festa.view.events.viewmodel.eventlist.EventListResponse
import com.example.festa.view.events.viewmodel.particularuserlist.ParticularUserEventListResponse
import com.example.festa.view.logins.viewmodel.login.LoginBody
import com.example.festa.view.logins.viewmodel.login.LoginResponse
import com.example.festa.view.profile.viewmodel.feedback.FeedBackBody
import com.example.festa.view.profile.viewmodel.feedback.FeedBackResponse
import com.example.festa.view.profile.viewmodel.getprofile.GetProfileResponse
import com.example.festa.view.profile.viewmodel.updateprofile.UserUpdateResponse
import com.example.festa.view.signup.viewmodel.SignUpResponse
import com.example.festa.view.subeventsupdate.viewmodel.subeventdetails.SubEventResponse
import com.example.festa.view.subeventsupdate.viewmodel.subeventupdate.SubEventUpdateBody
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiServices {
    @Multipart
    @Headers("Accept:application/json")
    @POST("userSignUp")
    fun customerRegister(
        @Part("fullName") fullName: RequestBody,
        @Part("phone_no") phone_no: RequestBody,
        @Part Attachment: MultipartBody.Part
    ): Observable<SignUpResponse>

    @Headers("Accept:application/json")
    @POST("userLogin")
    fun getuserLogin(
        @Body body: LoginBody
    ): Observable<LoginResponse>


    @Multipart
    @Headers("Accept:application/json")
    @POST("create_Event/{userId}")
    fun createEvent(
        @Path("userId") userId: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("event_Type") event_Type: RequestBody,
        @Part("venue_Date_and_time") venue_Date_and_time: RequestBody,
        @Part cloth_image: Array<MultipartBody.Part?>
    ): Observable<CreateEventReponse>

    @GET("getUserEvent/{userId}")
    fun getEventList(
        @Path("userId") userId: String
    ): Observable<EventListResponse>

    @GET("getUserEvent/{userId}")
    fun getEventParticularList(
        @Path("userId") userId: String
    ): Observable<ParticularUserEventListResponse>

    @Headers("Accept:application/json")
    @POST("newVenue_Date_Time/{userId}")
    fun addNewVenue(
        @Path("userId") userId: String,
        @Body body: AddVenueBody
    ): Observable<AddVenueResponse>

    @Headers("Accept:application/json")
    @POST("add_guest/{eventId}")
    fun addGuest(
        @Path("eventId") eventId: String,
        @Body body: GuestBody
    ): Observable<GuestResponse>

    @Headers("Accept:application/json")
    @POST("add_co_host/{eventId}")
    fun addCoHost(
        @Path("eventId") eventId: String,
        @Body body: AddCoHostBody
    ): Observable<GuestResponse>

    @GET("getAllGuest/{eventId}")
    fun getAllGuests(
        @Path("eventId") eventId: String
    ): Observable<GuestListResponse>

    @GET("getAll_co_Hosts/{eventId}")
    fun getCoHostGuests(
        @Path("eventId") eventId: String
    ): Observable<CostHostListResponse>

    @Multipart
    @Headers("Accept:application/json")
    @POST("import_Guest/{eventId}")
    fun uploadExcel(
        @Path("eventId") eventId: String,
        @Part Attachment: MultipartBody.Part
    ): Observable<UploadexcelResponse>

    @Headers("Accept:application/json")
    @POST("addAllGuestsToBookmark/{eventId}")
    fun bookMark(
        @Path("eventId") eventId: String,
        @Body body: BookMarkPostBody
    ): Observable<BookMarkPostResponse>

    @GET("getAllCollections/{eventId}")
    fun getBookMark(
        @Path("eventId") eventId: String
    ): Observable<BookMarkGetResponse>

    @GET("getCollectionById/{collectionId}")
    fun getCollectionGuest(
        @Path("collectionId") collectionId: String
    ): Observable<BookMarkNameResponse>

    @Headers("Accept:application/json")
    @POST("feedback/{userId}/{eventId}")
    fun feedback(
        @Path("userId") userId: String,
        @Path("eventId") eventId: String,
        @Body body: FeedBackBody
    ): Observable<FeedBackResponse>

    @GET("getEvent/{eventId}")
    fun getEdit(
        @Path("eventId") eventId: String
    ): Observable<GetEditResponse>

    /* @Headers("Accept:application/json")
     @POST("updateEvent/{eventId}")
     fun createFinalEvent(
         @Path("eventId") eventId: String,
         @Body body: CreateFinalEventBody
     ): Observable<SendInviteResponse>*/
    @Multipart
    @Headers("Accept:application/json")
    @POST("updateEvent/{eventId}")
    fun createFinalEvent(
        @Path("eventId") eventId: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("event_Type ") event_Type: RequestBody,
        @Part("venue_Name") venue_Name: RequestBody,
        @Part("venue_location") venue_location: RequestBody,
        @Part("date") date: RequestBody,
        @Part("start_time") start_time: RequestBody,
        @Part("end_time") end_time: RequestBody,
        @Part("event_key") event_key: RequestBody,
        @Part cloth_image: Array<MultipartBody.Part?>
    ): Observable<SendInviteResponse>

    @Multipart
    @Headers("Accept:application/json")
    @POST("updateEvent/{eventId}")
    fun createMultipleEvent(
        @Path("eventId") userId: String,
        @Part("title") title: RequestBody,
        @Part("description") description: RequestBody,
        @Part("event_Type") event_Type: RequestBody,
        @Part("venue_Date_and_time") venue_Date_and_time: RequestBody,
        @Part("event_key") event_key: RequestBody,
        @Part image: Array<MultipartBody.Part?>
    ): Observable<SendInviteResponse>

    @GET("sendInvitation/{eventId}")
    fun sendInvite(
        @Path("eventId") userId: String
    ): Observable<SendInviteResponse>

    @DELETE("delete_Guest/{eventId}/{guestId}")
    fun deleteGuest(
        @Path("eventId") eventId: String,
        @Path("guestId") guestId: String
    ): Observable<BookMarkPostResponse>

    @DELETE("delete_co_Host/{co_hostId}/{eventId}")
    fun deleteCoHost(
        @Path("co_hostId") co_hostId: String,
        @Path("eventId") eventId: String

    ): Observable<BookMarkPostResponse>

    @Headers("Accept:application/json")
    @POST("numberExistance")
    fun verifyPhoneNumber(
        @Body body: LoginBody
    ): Observable<SendInviteResponse>

    @DELETE("delete_Venue_Date_Time/{venueId}/{eventId}")
    fun deleteMultipleEvent(
        @Path("venueId") venueId: String,
        @Path("eventId") eventId: String

    ): Observable<BookMarkPostResponse>

    @GET("getSubEventOf_Event/{subEventId}/{eventId}")
    fun deleteSubEventDetails(
        @Path("subEventId") subEventId: String,
        @Path("eventId") eventId: String
    ): Observable<SubEventResponse>

    @Headers("Accept:application/json")
    @PUT("edit_Venue_Date_Time/{venueId}/{eventId}")
    fun updateSubEvent(
        @Path("venueId") userId: String,
        @Path("eventId") eventId: String,
        @Body body: SubEventUpdateBody
    ): Observable<BookMarkPostResponse>

    @GET("getUser/{userId}")
    fun getProfileDetails(
        @Path("userId") userId: String
    ): Observable<GetProfileResponse>

    @Multipart
    @Headers("Accept:application/json")
    @POST("updateUser/{userId}")
    fun updateUserDetails(
        @Path("userId") userId: String,
        @Part("fullName") fullName: RequestBody,
        @Part("phone_no") phone_no: RequestBody,
        @Part("email") email: RequestBody,
        @Part profileImage: MultipartBody.Part
    ): Observable<UserUpdateResponse>

    /**
     * Show All Album
     */
    @GET("getAllAlbum/{eventId}")
    fun getAllAlbum(
        @Path("eventId") userId: String
    ): Observable<AllAlbumResponse>

    /**
     * Create New Album
     */
    @Headers("Accept:application/json")
    @POST("createEventAlbum/{eventId}")
    fun createNewAlbum(
        @Path("eventId") userId: String,
        @Body params: CreateAlbumBody
    ): Observable<CreateNewAlbumResponse>

    @DELETE("deleteGuestInCollection/{collection_id}/{guestId}")
    fun deleteGuestBook(
        @Path("collection_id") co_hostId: String,
        @Path("guestId") guestId: String

    ): Observable<BookMarkPostResponse>

}